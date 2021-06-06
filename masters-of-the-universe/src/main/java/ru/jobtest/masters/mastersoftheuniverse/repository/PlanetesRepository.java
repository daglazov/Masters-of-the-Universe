package ru.jobtest.masters.mastersoftheuniverse.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jobtest.masters.mastersoftheuniverse.domain.Planet;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class PlanetesRepository implements CommonRepository<Planet> {

    private static final String SQL_INSERT = "INSERT INTO planetes(id, name, master_id) VALUES (:id, :name, :masterId)";
    private static final String SQL_QUERY_FIND_ALL = "SELECT * FROM planetes AS p";
    private static final String SQL_QUERY_FIND_BY_ID = SQL_QUERY_FIND_ALL + " WHERE id = :id";
    private static final String SQL_UPDATE = "UPDATE planetes SET name = :name, master_id = :masterId WHERE id = :id";
    private static final String SQL_DELETE = "DELETE FROM planetes WHERE id = :id";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public PlanetesRepository(NamedParameterJdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private RowMapper<Planet> PlanetRowMapper = (ResultSet rs, int rowNumber) -> {
        Planet planet = new Planet();
        planet.setId(rs.getString("id"));
        planet.setName(rs.getString("name"));
        planet.setMasterId(rs.getString("master_id"));
        return planet;
    };

    @Override
    public Planet save( final Planet domain) {
        Planet result = findById(domain.getId());
        if (result != null){
            result.setId(domain.getId());
            result.setName(domain.getName());
            result.setMasterId(domain.getMasterId());
            return upsert(result, SQL_UPDATE);
        }
        return upsert(domain, SQL_INSERT);
    }

    private Planet upsert(final Planet domain, final String sqlInsert) {
        Map<String, Object> namedParameters = new HashMap<>();
        namedParameters.put("id", domain.getId());
        namedParameters.put("name", domain.getName());
        namedParameters.put("masterId", domain.getMasterId());

        this.jdbcTemplate.update(sqlInsert, namedParameters);
        return findById(domain.getId());
    }

    @Override
    public Iterable<Planet> save(Collection<Planet> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(Planet domain) {
        Map<String, String> namedParameters = Collections.singletonMap("id", domain.getId());
        this.jdbcTemplate.update(SQL_DELETE, namedParameters);
    }

    @Override
    public Planet findById(String id) {
        try {
            Map<String, String> namedParameters = Collections.singletonMap("id", id);
            return this.jdbcTemplate.queryForObject(SQL_QUERY_FIND_BY_ID, namedParameters, PlanetRowMapper);
        }catch (EmptyResultDataAccessException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Iterable<Planet> findAll() {
        return this.jdbcTemplate.query(SQL_QUERY_FIND_ALL, PlanetRowMapper);
    }

    @Override
    public Iterable<Planet> findIdle() {
        return null;
    }

    @Override
    public Iterable<Planet> findTopYoung() {
        return null;
    }
}
