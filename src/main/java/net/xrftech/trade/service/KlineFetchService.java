package net.xrftech.trade.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class KlineFetchService {
    
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    
    public KlineFetchService(@Value("${binance.testnet.base-url}") String baseUrl) {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        this.baseUrl = baseUrl;
        log.info("KlineFetchService initialized with base URL: {}", baseUrl);
    }
    
    public List<BinanceKline> fetchKlines(String symbol, String interval) {
        try {
            String url = String.format("%s/api/v3/klines?symbol=%s&interval=%s&limit=100", 
                    baseUrl, symbol.replace("/", ""), interval);
            
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new RuntimeException("Failed to fetch K-lines: " + response.code());
                }
                
                String responseBody = response.body().string();
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                
                List<BinanceKline> klines = new ArrayList<>();
                for (JsonNode klineNode : jsonNode) {
                    BinanceKline kline = parseKline(klineNode);
                    if (isValidKline(kline)) {
                        klines.add(kline);
                    }
                }
                
                log.info("Fetched {} valid K-lines for {} at interval {}", klines.size(), symbol, interval);
                return klines;
            }
        } catch (IOException e) {
            log.error("Failed to fetch K-lines for {}: {}", symbol, e.getMessage());
            throw new RuntimeException("K-line fetch failed", e);
        }
    }
    
    private BinanceKline parseKline(JsonNode klineNode) {
        BinanceKline kline = new BinanceKline();
        kline.setOpenTime(klineNode.get(0).asLong());
        kline.setOpen(Double.parseDouble(klineNode.get(1).asText()));
        kline.setHigh(Double.parseDouble(klineNode.get(2).asText()));
        kline.setLow(Double.parseDouble(klineNode.get(3).asText()));
        kline.setClose(Double.parseDouble(klineNode.get(4).asText()));
        kline.setVolume(Double.parseDouble(klineNode.get(5).asText()));
        kline.setCloseTime(klineNode.get(6).asLong());
        return kline;
    }
    
    private boolean isValidKline(BinanceKline kline) {
        if (kline.getOpen() == null || kline.getHigh() == null || kline.getLow() == null ||
            kline.getClose() == null || kline.getVolume() == null) {
            log.warn("Invalid K-line: null values detected");
            return false;
        }
        if (kline.getOpen() < 0 || kline.getHigh() < 0 || kline.getLow() < 0 || 
            kline.getClose() < 0 || kline.getVolume() < 0) {
            log.warn("Invalid K-line: negative values detected");
            return false;
        }
        return true;
    }
    
    public static class BinanceKline {
        private Long openTime;
        private Double open;
        private Double high;
        private Double low;
        private Double close;
        private Double volume;
        private Long closeTime;
        
        // Getters and Setters
        public Long getOpenTime() { return openTime; }
        public void setOpenTime(Long openTime) { this.openTime = openTime; }
        
        public Double getOpen() { return open; }
        public void setOpen(Double open) { this.open = open; }
        
        public Double getHigh() { return high; }
        public void setHigh(Double high) { this.high = high; }
        
        public Double getLow() { return low; }
        public void setLow(Double low) { this.low = low; }
        
        public Double getClose() { return close; }
        public void setClose(Double close) { this.close = close; }
        
        public Double getVolume() { return volume; }
        public void setVolume(Double volume) { this.volume = volume; }
        
        public Long getCloseTime() { return closeTime; }
        public void setCloseTime(Long closeTime) { this.closeTime = closeTime; }
    }
} 