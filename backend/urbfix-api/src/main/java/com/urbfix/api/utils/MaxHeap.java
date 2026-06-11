package com.urbfix.api.utils;

import com.urbfix.api.models.Chamado;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaxHeap {

    private final List<Chamado> heap = new ArrayList<>();

    public void insert(Chamado chamado) {
        heap.add(chamado);
        siftUp(heap.size() - 1);
    }

    public Optional<Chamado> extractMax() {
        if (heap.isEmpty()) return Optional.empty();
        Chamado max = heap.get(0);
        int last = heap.size() - 1;
        heap.set(0, heap.get(last));
        heap.remove(last);
        if (!heap.isEmpty()) siftDown(0);
        return Optional.of(max);
    }

    public void removeById(Long id) {
        int index = -1;
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).getId().equals(id)) {
                index = i;
                break;
            }
        }
        if (index == -1) return;
        int last = heap.size() - 1;
        heap.set(index, heap.get(last));
        heap.remove(last);
        if (index < heap.size()) {
            siftUp(index);
            siftDown(index);
        }
    }

    public List<Chamado> toSortedList() {
        List<Chamado> sorted = new ArrayList<>(heap);
        sorted.sort((a, b) -> Double.compare(b.getScorePrioridade(), a.getScorePrioridade()));
        return sorted;
    }

    public void clear() {
        heap.clear();
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void siftUp(int i) {
        while (i > 0) {
            int parent = (i - 1) / 2;
            if (heap.get(i).getScorePrioridade() > heap.get(parent).getScorePrioridade()) {
                swap(i, parent);
                i = parent;
            } else {
                break;
            }
        }
    }

    private void siftDown(int i) {
        int size = heap.size();
        while (true) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            int largest = i;
            if (left < size && heap.get(left).getScorePrioridade() > heap.get(largest).getScorePrioridade()) {
                largest = left;
            }
            if (right < size && heap.get(right).getScorePrioridade() > heap.get(largest).getScorePrioridade()) {
                largest = right;
            }
            if (largest == i) break;
            swap(i, largest);
            i = largest;
        }
    }

    private void swap(int a, int b) {
        Chamado tmp = heap.get(a);
        heap.set(a, heap.get(b));
        heap.set(b, tmp);
    }
}
