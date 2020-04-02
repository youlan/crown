package org.crown.repository;

import org.crown.domain.RequestPoint;

import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for the RequestPoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RequestPointRepository extends MongoRepository<RequestPoint, String> {
    List<RequestPoint> findByPositionWithin(Circle circle);

    List<RequestPoint> findByPositionNear(Point point, Distance distance);
}
