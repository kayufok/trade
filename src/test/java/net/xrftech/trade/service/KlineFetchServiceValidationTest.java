package net.xrftech.trade.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "binance.testnet.base-url=https://testnet.binance.vision"
})
class KlineFetchServiceValidationTest {

    @Autowired
    private KlineFetchService klineFetchService;

    private List<KlineFetchService.BinanceKline> testKlines;

    @BeforeEach
    void setUp() {
        testKlines = new ArrayList<>();
    }

    @Test
    void testValidKlineValidation() {
        // Create a valid K-line
        KlineFetchService.BinanceKline validKline = createValidKline();
        
        // Test that valid K-line passes validation
        assertTrue(isValidKline(validKline));
    }

    @Test
    void testNullValuesValidation() {
        // Test null open price
        KlineFetchService.BinanceKline kline = createValidKline();
        kline.setOpen(null);
        assertFalse(isValidKline(kline));

        // Test null high price
        kline = createValidKline();
        kline.setHigh(null);
        assertFalse(isValidKline(kline));

        // Test null low price
        kline = createValidKline();
        kline.setLow(null);
        assertFalse(isValidKline(kline));

        // Test null close price
        kline = createValidKline();
        kline.setClose(null);
        assertFalse(isValidKline(kline));

        // Test null volume
        kline = createValidKline();
        kline.setVolume(null);
        assertFalse(isValidKline(kline));
    }

    @Test
    void testNegativeValuesValidation() {
        // Test negative open price
        KlineFetchService.BinanceKline kline = createValidKline();
        kline.setOpen(-100.0);
        assertFalse(isValidKline(kline));

        // Test negative high price
        kline = createValidKline();
        kline.setHigh(-100.0);
        assertFalse(isValidKline(kline));

        // Test negative low price
        kline = createValidKline();
        kline.setLow(-100.0);
        assertFalse(isValidKline(kline));

        // Test negative close price
        kline = createValidKline();
        kline.setClose(-100.0);
        assertFalse(isValidKline(kline));

        // Test negative volume
        kline = createValidKline();
        kline.setVolume(-100.0);
        assertFalse(isValidKline(kline));
    }

    @Test
    void testLogicalConsistencyValidation() {
        // Test high < low
        KlineFetchService.BinanceKline kline = createValidKline();
        kline.setHigh(45000.0);
        kline.setLow(46000.0);
        assertFalse(isValidKline(kline));

        // Test high < open
        kline = createValidKline();
        kline.setHigh(45000.0);
        kline.setOpen(46000.0);
        assertFalse(isValidKline(kline));

        // Test high < close
        kline = createValidKline();
        kline.setHigh(45000.0);
        kline.setClose(46000.0);
        assertFalse(isValidKline(kline));

        // Test low > open
        kline = createValidKline();
        kline.setLow(47000.0);
        kline.setOpen(46000.0);
        assertFalse(isValidKline(kline));

        // Test low > close
        kline = createValidKline();
        kline.setLow(47000.0);
        kline.setClose(46000.0);
        assertFalse(isValidKline(kline));
    }

    @Test
    void testOutlierValidation() {
        // Test extremely high prices
        KlineFetchService.BinanceKline kline = createValidKline();
        kline.setOpen(2000000.0);
        assertFalse(isValidKline(kline));

        kline = createValidKline();
        kline.setHigh(2000000.0);
        assertFalse(isValidKline(kline));

        kline = createValidKline();
        kline.setLow(2000000.0);
        assertFalse(isValidKline(kline));

        kline = createValidKline();
        kline.setClose(2000000.0);
        assertFalse(isValidKline(kline));
    }

    private KlineFetchService.BinanceKline createValidKline() {
        KlineFetchService.BinanceKline kline = new KlineFetchService.BinanceKline();
        kline.setOpenTime(System.currentTimeMillis());
        kline.setOpen(45000.0);
        kline.setHigh(46000.0);
        kline.setLow(44000.0);
        kline.setClose(45500.0);
        kline.setVolume(100.0);
        kline.setCloseTime(System.currentTimeMillis() + 60000);
        return kline;
    }

    // Helper method to access the private validation method
    private boolean isValidKline(KlineFetchService.BinanceKline kline) {
        // Use reflection to access the private method
        try {
            java.lang.reflect.Method method = KlineFetchService.class.getDeclaredMethod("isValidKline", KlineFetchService.BinanceKline.class);
            method.setAccessible(true);
            return (Boolean) method.invoke(klineFetchService, kline);
        } catch (Exception e) {
            throw new RuntimeException("Failed to test validation", e);
        }
    }
} 