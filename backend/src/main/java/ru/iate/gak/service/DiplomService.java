package ru.iate.gak.service;

import ru.iate.gak.model.DiplomEntity;

public interface DiplomService {
    DiplomEntity getDiplomBySpeakerId(Long id);
}
