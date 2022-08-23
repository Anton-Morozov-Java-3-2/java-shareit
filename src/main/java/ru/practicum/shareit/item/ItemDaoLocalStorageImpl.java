package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class ItemDaoLocalStorageImpl implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private Long counterId = 0L;

    private Long createId() {
        return ++counterId;
    }

    private boolean isItemExists(Long id) {
        return items.containsKey(id);
    }

    private void checkItemExists(Long id) throws ItemNotFoundException {
        if (!isItemExists(id)) {
            String info = ItemNotFoundException.createMessage(id);
            log.info(info);
            throw new ItemNotFoundException(info);
        }
    }

    @Override
    public Item crate(Item item) {
        item.setId(createId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item read(Long id) throws ItemNotFoundException {
        checkItemExists(id);
        return items.get(id);
    }

    @Override
    public List<Item> readAllItemsOwner(Long idOwner) {
        return items.values().stream()
                .filter(item -> item.getOwner().getId().equals(idOwner))
                .collect(Collectors.toList());
    }

    @Override
    public Item update(Long id, Item item) throws ItemNotFoundException, ItemAccessException {
        checkItemExists(id);
        Item dbItem = items.get(id);
        if (dbItem.getOwner().getId().equals(item.getOwner().getId())) {
            if (item.getAvailable() != null) {
                dbItem.setAvailable(item.getAvailable());
            }

            if (item.getName() != null) {
                dbItem.setName(item.getName());
            }

            if (item.getDescription() != null) {
                dbItem.setDescription(item.getDescription());
            }

            if (item.getRequest() != null) {
                dbItem.setRequest(item.getRequest());
            }

            delete(id);
            items.put(id, dbItem);
            return dbItem;
        } else {
            String info = ItemAccessException.createMessage(item.getOwner().getId(), id);
            throw new ItemAccessException(info);
        }
    }

    @Override
    public void delete(Long id) throws ItemNotFoundException {
        checkItemExists(id);
        items.remove(id);
    }

    @Override
    public List<Item> searchItem(String text) {
        String target = text.strip().toLowerCase();
        if (target.isBlank()) {
            return new ArrayList<>();
        } else {
            return items.values().stream()
                    .filter(item -> (item.getDescription().toLowerCase().contains(target)
                            || item.getName().toLowerCase().contains(target))
                            && item.getAvailable())
                    .collect(Collectors.toList());
        }
    }
}
