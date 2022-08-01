package com.hiberus.show.sink.service;


import com.hiberus.show.library.topology.OutputShowListEvent;
import com.hiberus.show.library.topology.OutputShowListKey;

public interface ShowSinkService {

    void performOperation(final OutputShowListKey key, final OutputShowListEvent value);
}
