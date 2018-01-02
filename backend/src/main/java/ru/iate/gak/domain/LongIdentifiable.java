package ru.iate.gak.domain;

public class LongIdentifiable {

    private Long id;

    public LongIdentifiable() {
    }

    public LongIdentifiable(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
