package net.xrftech.trade.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("kline")
public class Kline {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
} 