package com.urbfix.api.services;

import com.urbfix.api.dtos.ChamadoRequestDTO;
import com.urbfix.api.exceptions.ResourceNotFoundException;
import com.urbfix.api.exceptions.TransicaoInvalidaException;
import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.models.Chamado;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.models.EstadoChamado;
import com.urbfix.api.repositories.CategoriaProblemaRepository;
import com.urbfix.api.repositories.ChamadoRepository;
import com.urbfix.api.repositories.CidadaoRepository;
import com.urbfix.api.utils.ChamadoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final CidadaoRepository cidadaoRepository;
    private final CategoriaProblemaRepository categoriaProblemaRepository;
    private final ChamadoFactory chamadoFactory;
    private final FilaPrioridadeService filaPrioridadeService;

    public Chamado criarChamado(ChamadoRequestDTO dto) {
        Cidadao cidadao;
        if (dto.getCidadaoId() != null) {
            cidadao = cidadaoRepository.findById(dto.getCidadaoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cidadão não encontrado com id: " + dto.getCidadaoId()));
        } else {
            cidadao = cidadaoRepository.findByDispositivoId(dto.getDispositivoId())
                    .orElseGet(() -> cidadaoRepository.save(
                            Cidadao.builder()
                                    .nome("Anônimo")
                                    .dispositivoId(dto.getDispositivoId())
                                    .build()
                    ));
        }

        CategoriaProblema categoria = categoriaProblemaRepository.findById(dto.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com id: " + dto.getCategoriaId()));

        Chamado chamado = chamadoFactory.criarChamado(cidadao, categoria, dto.getLatitude(), dto.getLongitude());
        if (dto.getImagemBase64() != null) {
            chamado.setImagemBase64(dto.getImagemBase64());
        }

        Chamado saved = chamadoRepository.save(chamado);
        filaPrioridadeService.enfileirar(saved);
        return saved;
    }

    public Chamado atualizarStatus(Long id, EstadoChamado novoEstado) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado com id: " + id));

        EstadoChamado estadoAtual = chamado.getEstado();
        if (!estadoAtual.podeTransicionarPara(novoEstado)) {
            throw new TransicaoInvalidaException(
                    "Transição inválida: %s -> %s".formatted(estadoAtual, novoEstado)
            );
        }

        chamado.setEstado(novoEstado);
        Chamado saved = chamadoRepository.save(chamado);
        if (novoEstado == EstadoChamado.ABERTO) {
            if (saved.getCategoria() != null) {
                saved.getCategoria().getNome();
            }
            if (saved.getCidadao() != null) {
                saved.getCidadao().getNome();
            }
            filaPrioridadeService.enfileirar(saved);
        } else {
            filaPrioridadeService.remover(id);
        }
        return saved;
    }

    @Transactional(readOnly = true)
    public List<Chamado> listarTodos() {
        return chamadoRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional(readOnly = true)
    public List<Chamado> listarPorEstado(EstadoChamado estado) {
        return chamadoRepository.findByEstadoOrderByCreatedAtDesc(estado);
    }

    @Transactional(readOnly = true)
    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Chamado não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public java.util.Map<EstadoChamado, Long> contagemPorEstado() {
        java.util.Map<EstadoChamado, Long> contagem = new java.util.EnumMap<>(EstadoChamado.class);
        for (EstadoChamado estado : EstadoChamado.values()) {
            contagem.put(estado, chamadoRepository.countByEstado(estado));
        }
        return contagem;
    }

    @Transactional(readOnly = true)
    public List<Chamado> listarPorCidadao(Long cidadaoId) {
        if (!cidadaoRepository.existsById(cidadaoId)) {
            throw new ResourceNotFoundException("Cidadão não encontrado com id: " + cidadaoId);
        }
        return chamadoRepository.findByCidadaoIdOrderByCreatedAtDesc(cidadaoId);
    }
}
