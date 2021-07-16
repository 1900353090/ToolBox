package cn.wcy.util;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class LotteryUtil {

    @Data
    public static class Prize {
        //奖品id
        private long id;
        //概率
        private BigDecimal probability;
    }

    @Data
    private static class Axis {
        private long id;
        private BigDecimal min;
        private BigDecimal max;
    }

    private static List<Axis> axis(List<Prize> prizes) {
        if (CollectionUtils.isEmpty(prizes)) {
            return new ArrayList<>(0);
        }
        //根据概率自然顺序
        prizes = prizes.parallelStream().sorted(Comparator.comparing(Prize::getProbability)).collect(Collectors.toList());
        List<Axis> axisList = new LinkedList<>();
        BigDecimal min = new BigDecimal(0), max = new BigDecimal(0);
        for (int i=0;i<prizes.size();i++) {
            Axis axis = new Axis();
            axis.setId(prizes.get(i).id);
            min = max;
            max = max.add(prizes.get(i).probability);
            axis.setMin(min);
            axis.setMax(max);
            axisList.add(axis);
        }
        return axisList;
    }

    public static long lottery(List<Prize> prizes) {
        List<Axis> axis = axis(prizes);
        System.out.println(axis);
        BigDecimal max = prizes.parallelStream().map(Prize::getProbability).reduce(BigDecimal::add).get();
        double random = RandomUtils.nextDouble(0, max.doubleValue());
        for (Axis axis1:axis) {
            if (random > axis1.min.doubleValue() && random < axis1.max.doubleValue()) {
                return axis1.id;
            }
        }
        return 0;
    }

}
