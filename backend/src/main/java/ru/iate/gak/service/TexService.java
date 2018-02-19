package ru.iate.gak.service;

import java.io.File;
import java.util.Map;

public interface TexService {
    File exportDocuments(Map<String, String> params);
}
