package ru.jobtest.masters.mastersoftheuniverse.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.jobtest.masters.mastersoftheuniverse.domain.Master;
import ru.jobtest.masters.mastersoftheuniverse.domain.MasterBuilder;
import ru.jobtest.masters.mastersoftheuniverse.domain.Planet;
import ru.jobtest.masters.mastersoftheuniverse.domain.PlanetBuilder;
import ru.jobtest.masters.mastersoftheuniverse.repository.MastersRepository;
import ru.jobtest.masters.mastersoftheuniverse.repository.PlanetesRepository;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@WebMvcTest(MastersAndPlanetesController.class)
class MastersAndPlanetesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MastersRepository mastersRepository;
    @MockBean
    private PlanetesRepository planetesRepository;

    @Test
    void getIdleMasters() throws Exception {
        List<Master> ret = new LinkedList<>();
        ret.add(MasterBuilder.create().withAll("0", "Wasya", "15").build());
        given(this.mastersRepository.findIdle())
                .willReturn(ret);

        this.mvc.perform(MockMvcRequestBuilders.get("/api/masters/idle").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":\"0\",\"name\":\"Wasya\",\"age\":\"15\"}]"));
    }

    @Test
    void getMostYoung() throws Exception {
        List<Master> ret = new LinkedList<>();
        ret.add(MasterBuilder.create().withAll("0","Wasya","15").build());
        given(this.mastersRepository.findTopYoung())
                .willReturn(ret);

        this.mvc.perform(MockMvcRequestBuilders.get("/api/masters/young").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .string("[{\"id\":\"0\",\"name\":\"Wasya\",\"age\":\"15\"}]"));
    }

    @Test
    void addMaster() throws Exception {
        Master masterToSave = MasterBuilder.create().withAll("0","Wasya","15").build();
        given(this.mastersRepository.save(masterToSave))
                .willReturn(masterToSave);

        this.mvc.perform(MockMvcRequestBuilders.post("/api/masters")
                .content("{\"id\":\"0\",\"name\":\"Wasya\",\"age\":\"15\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void addPlanet() throws Exception {
        Planet planetToSave = PlanetBuilder.create().withAll("0","Jelezyaka","0").build();
        given(this.planetesRepository.save(planetToSave))
                .willReturn(planetToSave);

        this.mvc.perform(MockMvcRequestBuilders.post("/api/planetes")
                .content("{\"id\":\"0\",\"name\":\"Jelezyaka\",\"masterId\":\"0\"}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    void setMaster() throws Exception {
        Planet planetToFind = PlanetBuilder.create().withAll("0","Jelezyaka",null).build();
        given(this.planetesRepository.findById("0"))
                .willReturn(planetToFind);
        Planet planetToSave = PlanetBuilder.create().withAll("0","Jelezyaka","0").build();
        given(this.planetesRepository.save(planetToSave)).willReturn(planetToSave);

        this.mvc.perform(MockMvcRequestBuilders.patch("/api/planetes/0").param("masterId", "0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Location", "http://localhost/api/planetes/0"));
    }

    @Test
    void delPlanetById() throws Exception {
//        given(this.planetesRepository.delete(PlanetBuilder.create().withId("0").build())).g;
        this.mvc.perform(MockMvcRequestBuilders.delete("/api/planetes/0")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}