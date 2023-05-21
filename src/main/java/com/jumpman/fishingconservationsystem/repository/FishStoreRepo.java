package com.jumpman.fishingconservationsystem.repository;

import com.jumpman.fishingconservationsystem.entity.Cooperative;
import com.jumpman.fishingconservationsystem.entity.Fish;
import com.jumpman.fishingconservationsystem.entity.FishStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface FishStoreRepo extends JpaRepository<FishStore,Integer> {

    //get fishes  upload by dedicated cooperative
    @Query("FROM FishStore WHERE cooperative.username=?1")
    List<FishStore> getFishUploadByCooperative(String username);
//    https://stackoverflow.com/questions/24279186/fetch-random-records-using-spring-data-jpa


    @Query("FROM FishStore s ,Fish f WHERE s.fish.name=?1 AND s.fish.id=f.id")
    List<FishStore> getFishesByName(String fishName);

    @Query(
            value = "SELECT * FROM fish_store_tables WHERE date_fish_uploaded BETWEEN ?1  AND ?2 AND cooperative_coop_id=?3",
            nativeQuery=true

    )
    List<FishStore> generateReportFromRepo(LocalDate reportFrom, LocalDate reportTo,Integer coopId);
    @Query(
            value = "SELECT * FROM fish_store_tables WHERE fish_fish_id=?1 order by rand() limit 1",
            nativeQuery = true
    )
    FishStore getCooperativeByFish(Integer fishId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM FishStore s where s.storeId=?1")
    void deleteByStoreId(Integer storeId);
    FishStore findByStoreId(Integer storeId);
}
