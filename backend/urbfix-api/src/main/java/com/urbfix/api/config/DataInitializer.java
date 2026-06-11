package com.urbfix.api.config;

import com.urbfix.api.dtos.ChamadoRequestDTO;
import com.urbfix.api.models.CategoriaProblema;
import com.urbfix.api.models.Cidadao;
import com.urbfix.api.repositories.CategoriaProblemaRepository;
import com.urbfix.api.repositories.CidadaoRepository;
import com.urbfix.api.services.ChamadoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoriaProblemaRepository categoriaRepository;
    private final CidadaoRepository cidadaoRepository;
    private final ChamadoService chamadoService;

    @Override
    public void run(String... args) {
        seedCategorias();
        seedChamadosDemo();
    }

    private void seedCategorias() {
        if (categoriaRepository.count() > 0) {
            log.info("[seed] Categorias já existem. Nada a fazer.");
            return;
        }
        List<CategoriaProblema> categorias = List.of(
                CategoriaProblema.builder().nome("Buraco em via").pesoBase(8.0).build(),
                CategoriaProblema.builder().nome("Iluminação pública").pesoBase(5.0).build(),
                CategoriaProblema.builder().nome("Saneamento / Lixo").pesoBase(6.5).build(),
                CategoriaProblema.builder().nome("Sinalização danificada").pesoBase(7.0).build(),
                CategoriaProblema.builder().nome("Calçada quebrada").pesoBase(4.0).build()
        );
        categoriaRepository.saveAll(categorias);
        log.info("[seed] {} categorias inseridas.", categorias.size());
    }

    private void seedChamadosDemo() {
        if (chamadoService.listarTodos().size() > 0) {
            log.info("[seed] Chamados já existem. Nada a fazer.");
            return;
        }

        Cidadao demo = cidadaoRepository.findByDispositivoId("demo-device-001")
                .orElseGet(() -> cidadaoRepository.save(
                        Cidadao.builder()
                                .nome("Maria Silva")
                                .email("maria.demo@urbfix.local")
                                .dispositivoId("demo-device-001")
                                .build()));

        List<CategoriaProblema> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) return;

        double[][] coords = {
                {-23.5505, -46.6333},
                {-23.5610, -46.6500},
                {-23.5440, -46.6280},
                {-23.5620, -46.6200},
                {-23.5475, -46.6420}
        };

        for (int i = 0; i < categorias.size(); i++) {
            ChamadoRequestDTO dto = new ChamadoRequestDTO();
            dto.setCidadaoId(demo.getId());
            dto.setCategoriaId(categorias.get(i).getId());
            dto.setLatitude(coords[i][0]);
            dto.setLongitude(coords[i][1]);
            chamadoService.criarChamado(dto);
        }

        log.info("[seed] {} chamados demo inseridos e enfileirados.", categorias.size());
    }
}
