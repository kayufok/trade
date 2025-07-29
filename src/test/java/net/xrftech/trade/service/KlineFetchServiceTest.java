package net.xrftech.trade.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "binance.testnet.base-url=https://testnet.binance.vision"
})
class KlineFetchServiceTest {

    @Autowired
    private KlineFetchService klineFetchService;

    @Test
    void testFetchKlines() {
        // Test fetching K-lines for BTCUSDT
        var klines = klineFetchService.fetchKlines("BTCUSDT", "1m");
        
        // Verify we got some K-lines
        assertNotNull(klines);
        assertFalse(klines.isEmpty());
        
        // Verify the first K-line has valid data
        var firstKline = klines.get(0);
        assertNotNull(firstKline.getOpenTime());
        assertNotNull(firstKline.getOpen());
        assertNotNull(firstKline.getHigh());
        assertNotNull(firstKline.getLow());
        assertNotNull(firstKline.getClose());
        assertNotNull(firstKline.getVolume());
        
        // Verify prices are positive
        assertTrue(firstKline.getOpen() > 0);
        assertTrue(firstKline.getHigh() > 0);
        assertTrue(firstKline.getLow() > 0);
        assertTrue(firstKline.getClose() > 0);
        assertTrue(firstKline.getVolume() >= 0);
    }
} 