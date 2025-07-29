package net.xrftech.trade.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xrftech.trade.model.Kline;
import net.xrftech.trade.service.KlineFetchService;
import net.xrftech.trade.service.KlineStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/kline")
@RequiredArgsConstructor
public class KlineController {
    
    private final KlineFetchService klineFetchService;
    private final KlineStorageService klineStorageService;
    
    @GetMapping("/fetch/{symbol}")
    public ResponseEntity<Map<String, Object>> fetchKlines(@PathVariable String symbol,
                                                          @RequestParam(defaultValue = "1m") String interval) {
        try {
            log.info("Fetching K-lines for symbol: {} with interval: {}", symbol, interval);
            
            List<KlineFetchService.BinanceKline> klines = klineFetchService.fetchKlines(symbol, interval);
            
            Map<String, Object> response = Map.of(
                "symbol", symbol,
                "interval", interval,
                "count", klines.size(),
                "status", "success"
            );
            
            log.info("Successfully fetched {} K-lines for {}", klines.size(), symbol);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to fetch K-lines for {}: {}", symbol, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage(), "status", "error"));
        }
    }
    
    @PostMapping("/fetch-and-store/{symbol}")
    public ResponseEntity<Map<String, Object>> fetchAndStoreKlines(@PathVariable String symbol,
                                                                  @RequestParam(defaultValue = "1m") String interval) {
        try {
            log.info("Fetching and storing K-lines for symbol: {} with interval: {}", symbol, interval);
            
            List<KlineFetchService.BinanceKline> klines = klineFetchService.fetchKlines(symbol, interval);
            klineStorageService.storeKlines(klines, symbol);
            
            Map<String, Object> response = Map.of(
                "symbol", symbol,
                "interval", interval,
                "fetched", klines.size(),
                "stored", klines.size(),
                "status", "success"
            );
            
            log.info("Successfully fetched and stored {} K-lines for {}", klines.size(), symbol);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to fetch and store K-lines for {}: {}", symbol, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage(), "status", "error"));
        }
    }
    
    @GetMapping("/stored/{symbol}")
    public ResponseEntity<Map<String, Object>> getStoredKlines(@PathVariable String symbol) {
        try {
            log.info("Retrieving stored K-lines for symbol: {}", symbol);
            
            List<Kline> klines = klineStorageService.getKlinesBySymbol(symbol);
            
            Map<String, Object> response = Map.of(
                "symbol", symbol,
                "count", klines.size(),
                "klines", klines,
                "status", "success"
            );
            
            log.info("Retrieved {} stored K-lines for {}", klines.size(), symbol);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Failed to retrieve stored K-lines for {}: {}", symbol, e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage(), "status", "error"));
        }
    }
} 