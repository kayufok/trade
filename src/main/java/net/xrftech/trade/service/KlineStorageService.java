package net.xrftech.trade.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xrftech.trade.mapper.KlineMapper;
import net.xrftech.trade.model.Kline;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KlineStorageService {
    
    private final KlineMapper klineMapper;
    
    public void storeKlines(List<KlineFetchService.BinanceKline> binanceKlines, String symbol) {
        List<Kline> entities = binanceKlines.stream()
                .map(binanceKline -> convertToEntity(binanceKline, symbol))
                .collect(Collectors.toList());
        
        if (!entities.isEmpty()) {
            int inserted = klineMapper.insertBatch(entities);
            log.info("Stored {} K-lines for symbol {}", inserted, symbol);
        }
    }
    
    private Kline convertToEntity(KlineFetchService.BinanceKline binanceKline, String symbol) {
        Kline entity = new Kline();
        entity.setSymbol(symbol);
        entity.setTimestamp(binanceKline.getOpenTime());
        entity.setOpen(binanceKline.getOpen());
        entity.setHigh(binanceKline.getHigh());
        entity.setLow(binanceKline.getLow());
        entity.setClose(binanceKline.getClose());
        entity.setVolume(binanceKline.getVolume());
        return entity;
    }
    
    public List<Kline> getKlinesBySymbol(String symbol) {
        return klineMapper.selectBySymbol(symbol);
    }
} 