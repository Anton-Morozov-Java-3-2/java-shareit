package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository  itemRequestRepository;

    private final UserRepository userRepository;

    @Override
    public ItemRequestDto create(Long requestorId, ItemRequestDto itemRequestDto) throws UserNotFoundException {
        User requestor = userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(requestorId)));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(requestor);

        itemRequest.setCreated(LocalDateTime.now().withNano(0));
        return  ItemRequestMapper.itemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findAllByRequestorId(Long requestorId) throws UserNotFoundException {
        userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(requestorId)));

            return itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(requestorId).stream()
                    .map(ItemRequestMapper::itemRequestDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAll(Long requestorId, Integer from, Integer size)
            throws UserNotFoundException {
        userRepository.findById(requestorId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(requestorId)));

        PageRequest pageRequest = PageRequest.of(from == null ? 0 : from / size, size == null ? Integer.MAX_VALUE : size,
                Sort.by("created").descending());

        return itemRequestRepository.findAllByRequestorIdNot(requestorId, pageRequest)
                .stream().map(ItemRequestMapper::itemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto findById(Long userId, Long requestId) throws UserNotFoundException,
            ItemRequestNotFoundException {
        userRepository.findById(userId).orElseThrow(() ->
                new UserNotFoundException(UserNotFoundException.createMessage(userId)));

        return ItemRequestMapper
                .itemRequestDto(itemRequestRepository.findById(requestId).orElseThrow(() ->
                        new ItemRequestNotFoundException((ItemNotFoundException.createMessage(requestId)))));
    }
}
