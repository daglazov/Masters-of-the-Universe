package ru.jobtest.masters.mastersoftheuniverse.repository;

import java.util.Collection;

public interface CommonRepository<T> {
    public T save(T domain);
    public Iterable<T> save(Collection<T> domains);
    public void delete(T domain);
    public T findById(String id);
//    public <K> T set(T domain1, K domain2);
    public Iterable<T> findAll();
    public Iterable<T> findIdle();
    public Iterable<T> findTopYoung();
}
