package org.crown.web.rest;

import org.crown.CrownApp;
import org.crown.domain.RequestPoint;
import org.crown.repository.RequestPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link RequestPointResource} REST controller.
 */
@SpringBootTest(classes = CrownApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class RequestPointRepositoryID {

    @Autowired
    private RequestPointRepository requestPointRepository;

    @BeforeEach
    public void initTest() {
        requestPointRepository.deleteAll();

    }

    @Autowired
    MongoTemplate template;

    @Test
    public void searchFindByPositionWithin() {
            template.indexOps(RequestPoint.class).ensureIndex( new GeospatialIndex("position") );
        requestPointRepository.save( getMockRequestPoint("A", new GeoJsonPoint( 0.001, -0.002)) );
        requestPointRepository.save( getMockRequestPoint("B", new GeoJsonPoint(  1, 1)) );
        requestPointRepository.save( getMockRequestPoint("C", new GeoJsonPoint(  0.5, 0.5)) );
        requestPointRepository.save( getMockRequestPoint("D", new GeoJsonPoint(  -0.5, -0.5)));

        List<RequestPoint> points = requestPointRepository.findByPositionWithin(new Circle(0,0, 0.75) );

        assertThat(points).isNotNull();
        }

    @Test
    public void findByPositionNear() {
        template.indexOps(RequestPoint.class).ensureIndex( new GeospatialIndex("position") );
        requestPointRepository.save( getMockRequestPoint("Berlin", new GeoJsonPoint( 13.405838, 52.531261) ));
        requestPointRepository.save( getMockRequestPoint("Cologne", new GeoJsonPoint(  6.921272, 50.960157)) );
        requestPointRepository.save( getMockRequestPoint("Düsseldorf", new GeoJsonPoint(  6.810036,	51.224088)) );

        Point DUSSELDORF = new Point(6.810036,	51.224088);
        List<RequestPoint> points = requestPointRepository.findByPositionNear(DUSSELDORF , new Distance(70, Metrics.KILOMETERS));

        assertThat(points).isNotNull();
        assertThat(points.size()).isEqualTo(2);
        assertThat(points.stream().map(RequestPoint::getName).collect(Collectors.joining(","))).isEqualTo("Düsseldorf,Cologne");
    }

 /*   Berlin	13.405838	52.531261
    Cologne	6.921272	50.960157
    Düsseldorf	6.810036	51.224088*/

    private RequestPoint getMockRequestPoint(String name, GeoJsonPoint point) {
        RequestPoint requestPoint = new RequestPoint();
        requestPoint.setPosition(new double[]{point.getX(), point.getY()});
        requestPoint.setName(name);
        requestPoint.setPrimaryContactName(name);
        requestPoint.setZip("0001");
        return requestPoint;
    }
}
