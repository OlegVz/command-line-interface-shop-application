package com.hybris.shop.view.consoleInputOutput;

import java.util.List;

public interface PrinterInterface<T> {
    void printLine(String line);

    void printTable(List<T> entity);
}
