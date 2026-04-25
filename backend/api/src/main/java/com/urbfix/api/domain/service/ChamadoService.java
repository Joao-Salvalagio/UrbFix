package com.urbfix.api.domain.service;

import com.urbfix.api.application.dto.ChamadoRequestDTO;
import com.urbfix.api.domain.exception.ResourceNotFoundException;
import com.urbfix.api.domain.model.CategoriaProblema;
import com.urbfix.api.domain.model.Chamado;
import com.urbfix.api.domain.model.Cidadao;
import com.urbfix.api.domain.model.EstadoChamado;
import com.urbfix.api.domain.repository.CategoriaProblemaRepository;
import com.urbfix.api.domain.repository.ChamadoRepository;
import com.urbfix.api.domain.repository.CidadaoRepository;
import com.urbfix.api.infrastructure.factory.ChamadoFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final CategoriaProblemaRepository categoriaProblemaRepository;
    private final ChamadoFactory chamadoFactory;
    private final FilaPrioridadeService filaPrioridadeService;

    @PostConstruct
    public void init() {
        List<Chamado> abertos = chamadoRepository.findAllByEstado(EstadoChamado.ABERTO);
        abertos.forEach(filaPrioridadeService::enfileirar);
    }

    @Transactional
    public Chamado criarChamado(ChamadoRequestDTO dto) {
        Cidadao cidadao;
        if (dto.getCidadaoId() != null) {
            cidadao = cidadaoRepository.findById(dto.getCidadaoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com o ID: " + dto.getCidadaoId()));
        } else {
            cidadao = cidadaoRepository.findByDispositivoId(dto.getDispositivoId())
                    .orElseGet(() -> {
                        Cidadao novo = Cidadao.builder()
                                .nome("Anônimo")
                                .email("anonimo_" + UUID.randomUUID() + "@urbfix.com")
                                .dispositivoId(dto.getDispositivoId())
                                .build();
                        return cidadaoRepository.save(novo);
                    });
        }
        
        CategoriaProblema categoria = categoriaProblemaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria de problema não encontrada com o ID: " + dto.getCategoriaId()));

        Chamado chamado = chamadoFactory.criarChamado(cidadao, categoria, dto.getLatitude(), dto.getLongitude());
        chamado.setImagemBase64(dto.getImagemBase64());
        chamado = chamadoRepository.save(chamado);
        
        if (chamado.getEstado() == EstadoChamado.ABERTO) {
            filaPrioridadeService.enfileirar(chamado);
        }
        
        return chamado;
    }

    public List<Chamado> getFilaPrioridade() {
        return filaPrioridadeService.getFilaOrdenada();
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    @Transactional
    public Chamado atualizarStatus(Long id, EstadoChamado novoEstado) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado com o ID: " + id));

        EstadoChamado estadoAnterior = chamado.getEstado();
        chamado.setEstado(novoEstado);
        chamado = chamadoRepository.save(chamado);

        // Se saiu do estado ABERTO, remove da fila
        if (estadoAnterior == EstadoChamado.ABERTO && novoEstado != EstadoChamado.ABERTO) {
            filaPrioridadeService.remover(id);
        } 
        // Se voltou para o estado ABERTO (embora o State Pattern possa restringir isso), adiciona na fila
        else if (estadoAnterior != EstadoChamado.ABERTO && novoEstado == EstadoChamado.ABERTO) {
            filaPrioridadeService.enfileirar(chamado);
        }

        return chamado;
    }
}
