package net.xrftech.trade.mapper;

import net.xrftech.trade.model.Kline;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KlineMapper {
    
    /**
     * Batch insert K-lines
     * @param klines List of K-line data to insert
     * @return Number of rows affected
     */
    int insertBatch(@Param("klines") List<Kline> klines);
    
    /**
     * Insert a single K-line
     * @param kline K-line to insert
     * @return Number of rows affected
     */
    int insert(Kline kline);
    
    /**
     * Select K-lines by symbol
     * @param symbol Symbol to search for
     * @return List of K-lines
     */
    List<Kline> selectBySymbol(@Param("symbol") String symbol);
} 