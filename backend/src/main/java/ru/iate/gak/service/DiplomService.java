package ru.iate.gak.service;

import ru.iate.gak.domain.Diplom;

public interface DiplomService {
    Diplom getDiplomBySpeakerId(Long id);
}
