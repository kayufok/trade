package net.xrftech.trade.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.xrftech.trade.model.Kline;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KlineMapper extends BaseMapper<Kline> {

    /**
     * Select K-lines by symbol
     * @param symbol Symbol to search for
     * @return List of K-lines
     */
    List<Kline> selectBySymbol(@Param("symbol") String symbol);
} 