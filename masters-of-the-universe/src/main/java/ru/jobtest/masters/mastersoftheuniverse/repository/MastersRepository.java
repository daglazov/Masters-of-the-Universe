package ru.jobtest.masters.mastersoftheuniverse.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jobtest.masters.mastersoftheuniverse.domain.Master;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class MastersRepository implements CommonRepository<Master>{
    private static final String SQL_INSERT = "INSERT INTO masters(id, name, age) VALUES (:id, :name, :age)";
    private static final String SQL_QUERY_FIND_ALL = "SELECT * FROM masters AS m";
    private static final String SQL_QUERY_FIND_BY_ID = SQL_QUERY_FIND_ALL + " WHERE id = :id";
    private static final String SQL_QUERY_FIND_IDLE = SQL_QUERY_FIND_ALL + " LEFT JOIN planetes AS p " +
            "ON m.id = p.master_id WHERE p.master_id IS NULL";
    private static final String SQL_QUERY_FIND_YOUNG = SQL_QUERY_FIND_ALL + "ORDER BY age LIMIT 10";
    private static final String SQL_UPDATE = "UPDATE masters SET name = :name, age = :age WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public MastersRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Master> MasterRowMapper = (ResultSet rs, int rowNumber) -> {
        Master master = new Master();
        master.setId(rs.getString("id"));
        master.setName(rs.getString("name"));
        master.setAge(rs.getString("age"));
        return master;
    };


    @Override
    public Master save(final Master domain) {
        Master result = findById(domain.getId());
        if (result != null){
            result.setId(domain.getId());
            result.setName(domain.getName());
            result.setAge(domain.getAge());
            return upsert(result, SQL_UPDATE);
        }
        return upsert(domain, SQL_INSERT);
    }

    private Master upsert(final Master domain, final String sqlInsert) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", domain.getId());
        namedParameters.put("name", domain.getName());
        namedParameters.put("age", domain.getAge());

        this.jdbcTemplate.update(sqlInsert, namedParameters);
        return findById(domain.getId());
    }

    @Override
    public Iterable<Master> save(Collection<Master> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(Master domain) {

    }

    @Override
    public Master findById(String id) {
        try {
            Map<String, String> namedParameters = Collections.singletonMap("id", id);
            return this.jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, namedParameters, MasterRowMapper);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Master> findAll() {
        return this.jdbcTemplate.query(SQL_QUERY_FIND_ALL, MasterRowMapper);
    }

    @Override
    public Iterable<Master> findIdle() {
        return this.jdbcTemplate.query(SQL_QUERY_FIND_IDLE, MasterRowMapper);
    }

    @Override
    public Iterable<Master> findTopYoung() {
        return this.jdbcTemplate.query(SQL_QUERY_FIND_YOUNG, MasterRowMapper);
    }
}
