package org.jfl.strategy;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StrategyInspector {
    private final List<PaymentStrategy> strategies;

    public StrategyInspector(List<PaymentStrategy> strategies) {
        this.strategies = strategies;
    }

    public void printStrategies(){
        System.out.println("Number of strategies: " + strategies.size());
        strategies.forEach(
                s -> System.out.println(s.getClass().getName())
        );
    }
}
