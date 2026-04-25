package com.urbfix.api.domain.heap;

import com.urbfix.api.domain.model.Chamado;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MaxHeap {
    private List<Chamado> heap;

    public MaxHeap() {
        this.heap = new ArrayList<>();
    }

    public void insert(Chamado chamado) {
        heap.add(chamado);
        siftUp(heap.size() - 1);
    }

    public Optional<Chamado> extractMax() {
        if (heap.isEmpty()) {
            return Optional.empty();
        }

        Chamado max = heap.get(0);
        Chamado last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            siftDown(0);
        }

        return Optional.of(max);
    }

    public List<Chamado> getAllSorted() {
        List<Chamado> sortedList = new ArrayList<>();
        List<Chamado> originalHeap = new ArrayList<>(this.heap);
        
        while (!heap.isEmpty()) {
            extractMax().ifPresent(sortedList::add);
        }
        
        this.heap = originalHeap;
        return sortedList;
    }

    public void removeById(Long id) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).getId().equals(id)) {
                Chamado last = heap.remove(heap.size() - 1);
                if (i < heap.size()) {
                    heap.set(i, last);
                    siftUp(i);
                    siftDown(i);
                }
                break;
            }
        }
    }

    private void siftUp(int index) {
        while (index > 0) {
            int parentIndex = (index - 1) / 2;
            if (heap.get(index).getScorePrioridade() <= heap.get(parentIndex).getScorePrioridade()) {
                break;
            }
            swap(index, parentIndex);
            index = parentIndex;
        }
    }

    private void siftDown(int index) {
        int lastIndex = heap.size() - 1;
        while (true) {
            int leftChild = 2 * index + 1;
            int rightChild = 2 * index + 2;
            int largest = index;

            if (leftChild <= lastIndex && heap.get(leftChild).getScorePrioridade() > heap.get(largest).getScorePrioridade()) {
                largest = leftChild;
            }

            if (rightChild <= lastIndex && heap.get(rightChild).getScorePrioridade() > heap.get(largest).getScorePrioridade()) {
                largest = rightChild;
            }

            if (largest == index) {
                break;
            }

            swap(index, largest);
            index = largest;
        }
    }

    private void swap(int i, int j) {
        Chamado temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    public int size() {
        return heap.size();
    }
}
