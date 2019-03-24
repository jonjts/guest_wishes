package com.jonjts.guestwishes.repository;

import com.jonjts.guestwishes.model.Wish;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends CrudRepository<Wish, Long> {

    /**
     * Find a wish by guest
     * @param guestId
     * @return
     */
    public List<Wish> getByGuestId(Long guestId);

    /**
     * get the avg latitude
     * @param guestId
     * @return
     */
    @Query("select AVG(latitude) from wishes  where guest_id = :guest_id")
    public Double getLatitudeAverage(@Param("guest_id") Long guestId);

    /**
     * Get the avg longitude
     * @param guestId
     * @return
     */
    @Query("select AVG(longitude) from wishes  where guest_id = :guest_id")
    public Double getLongitudeAverage(@Param("guest_id") Long guestId);

}
