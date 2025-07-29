package net.xrftech.trade.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Kline {
    
    private Long id;
    private String symbol;
    private Long timestamp;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Double volume;
} 