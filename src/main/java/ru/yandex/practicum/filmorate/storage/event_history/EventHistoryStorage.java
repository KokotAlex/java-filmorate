package ru.yandex.practicum.filmorate.storage.event_history;

import ru.yandex.practicum.filmorate.model.EventHistory;

import java.util.List;

public interface EventHistoryStorage {
    List<EventHistory> findByUserId(int id);

    void save(EventHistory eventHistory);

}
