package net.xrftech.trade.service.db;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xrftech.trade.mapper.KlineMapper;
import net.xrftech.trade.model.Kline;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class KlineService extends ServiceImpl<KlineMapper, Kline> {



}
