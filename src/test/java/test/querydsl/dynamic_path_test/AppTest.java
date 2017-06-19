package test.querydsl.dynamic_path_test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.querydsl.core.types.dsl.Expressions;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AppTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DataRepository repo;

    @Test
    @SuppressWarnings("rawtypes")
    public void exampleTest() {
        Data data1 = new Data();
        data1.setValue(getMap("key1", "val1"));

        Data data2 = new Data();
        data2.setValue(getMap("key1", "val2"));

        repo.save(data1);
        repo.save(data2);

        Page<Data> findResult = repo.findAll(Expressions.stringPath("value.key1").eq("val1"), new PageRequest(0, 5));
        // This is fine
        Assert.assertEquals(1, findResult.getTotalElements());

        // k1 = Data.value.key1
        ResponseEntity<Map> response = this.restTemplate.getForEntity("/api/datas?k1=val1", Map.class);

        Assert.assertEquals(200, response.getStatusCodeValue()); // FAILS
        System.out.println(response.getBody()); // should be one entity
    }

    private Map<String, String> getMap(String key, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }
}
