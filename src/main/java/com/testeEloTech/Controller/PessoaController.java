package com.testeEloTech.Controller;

import com.testeEloTech.Model.PessoaModel;
import com.testeEloTech.Repository.PessoaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import com.testeEloTech.Api.response.GenericResponse;
import com.testeEloTech.Api.response.Request;
import com.testeEloTech.Model.ContatoModel;
import com.testeEloTech.Repository.ContatoDao;
import com.testeEloTech.Service.ContatoService;
import com.testeEloTech.Service.PessoaService;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.persistence.EntityManager;
import java.sql.Timestamp;

@RestController
@RequestMapping("/api")
public class PessoaController {
    @Autowired
    private PessoaService pessoaService;
    @Autowired
    private ContatoService contatoService;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private PessoaDao pessoaRepository;

    @Autowired
    private ContatoDao contatoRepository;

    @Configuration
    public class CorsConfig implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3001")
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowCredentials(true);
        }
    }

    @PostMapping(value = "/cadastro", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse> cadastrarPessoa(@RequestBody CadastroRequest cadastroRequest, @RequestHeader final MultiValueMap<String, Object> httpHeaders) {
        String detalhes = "";


        final GenericResponse response = new GenericResponse();
        final Request request = new Request();


        try {
            PessoaModel pessoa = cadastroRequest.getPessoaModel();
            String verificaCamposPessoa = pessoaService.verificaCamposPessoa(pessoa);

            if (!verificaCamposPessoa.equals("Pessoa Criada com Sucesso.")) {
                respostaChamada(verificaCamposPessoa, request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            if (!pessoaService.pessoaPersistida(pessoa)) {
                respostaChamada("Pessoa ja persistida anteriormente", request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            if (!pessoaService.isCpf(pessoa)) {
                respostaChamada("CPF Invalído", request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            if (!pessoaService.verificarDataNascimentoFutura(pessoa)) {
                respostaChamada("A Data de nascimento não pode ser uma data futura. ", request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            if (pessoa.getContatos() == null || pessoa.getContatos().isEmpty()) {
                respostaChamada("Pessoa sem nenhum contato associado", request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            for (ContatoModel contato : pessoa.getContatos()) {

                String statusMensagem = contatoService.verificaCamposContatos(contato);
                contato.setPessoa(pessoa);

                if (!"Contato Criado com Sucesso.".equals(statusMensagem)) {
                    respostaChamada(statusMensagem, request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
                if (contatoService.validarSintaxeEmail(contato)) {
                    respostaChamada("Email invalido", request, response, pessoa, detalhes, HttpStatus.INTERNAL_SERVER_ERROR.value());
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
                }
            }

            pessoaRepository.save(pessoa);
            contatoRepository.saveAll(pessoa.getContatos());

            respostaChamada("Pessoa Persistida", request, response, pessoa, detalhes, HttpStatus.CREATED.value());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping("/pessoa/{CPF}")
    public ResponseEntity<GenericResponse> buscarPessoaPorCPF(@PathVariable String CPF) {
        try {

            CPF = CPF.replace(".", "").trim();
            CPF = CPF.replace("-", "").trim();
            CPF = CPF.replace("/", "").trim();

            Optional<PessoaModel> pessoaOptional = pessoaRepository.findByCPF(CPF);
            final GenericResponse response = new GenericResponse();
            final Request request = new Request();

            if (pessoaOptional.isPresent() && Optional.empty().isEmpty()) {
                PessoaModel pessoa = pessoaOptional.get();

                respostaChamada("Pessoa encontrada", request, response, pessoa, "", HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
             response.setMessage("CPF não localizado");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/pessoas")
    public ResponseEntity<List<PessoaModel>> buscarPessoasPaginadas(@RequestParam(required = false) String filtro,
                                                                    @RequestParam(defaultValue = "0") int pagina,
                                                                    @RequestParam(defaultValue = "10") int tamanho) {
        try {
            Pageable pageable = PageRequest.of(pagina, tamanho);
            Page<PessoaModel> pessoas;

            if (filtro != null && !filtro.isEmpty()) {
                pessoas = pessoaRepository.findByNomeContainingIgnoreCase(filtro, pageable);
            } else {
                pessoas = (Page<PessoaModel>) pessoaRepository.findAll(pageable);
            }

            return ResponseEntity.ok(pessoas.getContent());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/pessoa/{CPF}")
    public ResponseEntity<GenericResponse> atualizarPessoa(@PathVariable String CPF,
                                                           @RequestBody PessoaModel pessoaAtualizada) {
        try {
            CPF = CPF.replace(".", "").trim();
            CPF = CPF.replace("-", "").trim();
            CPF = CPF.replace("/", "").trim();

            Optional<PessoaModel> pessoaOptional = pessoaRepository.findByCPF(CPF);

            if (pessoaOptional.isPresent()) {
                PessoaModel pessoa = pessoaOptional.get();
                if (!pessoaAtualizada.getNome().isEmpty() && pessoaAtualizada.getNome() != null)
                    pessoa.setNome(pessoaAtualizada.getNome());
                if (pessoaAtualizada.getCPF() != null && !pessoaAtualizada.getCPF().isEmpty())
                    pessoa.setCPF(pessoaAtualizada.getCPF());
                if (pessoaAtualizada.getDataNascimento() != null && !pessoaAtualizada.getDataNascimento().isEmpty())
                    pessoa.setDataNascimento(pessoaAtualizada.getDataNascimento());

                pessoaRepository.save(pessoa);

                final GenericResponse response = new GenericResponse();
                final Request request = new Request();
                respostaChamada("Pessoa atualizada", request, response, pessoa, "", HttpStatus.OK.value());
                return ResponseEntity.status(HttpStatus.OK).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/pessoa/{CPF}")
    public ResponseEntity<Void> deletarPessoaPorCPF(@PathVariable String CPF) {
        try {
            Optional<PessoaModel> pessoaOptional = pessoaRepository.findByCPF(CPF);

            Optional<ContatoModel> contatoModel = contatoRepository.findById(pessoaOptional.get().getId());

            List<ContatoModel> contatoModels = contatoRepository.findByIdContains(pessoaOptional.get().getId());

            if (pessoaOptional.isPresent()) {
                PessoaModel pessoa = pessoaOptional.get();

                pessoa.getContatos().size();

                for (ContatoModel contato : pessoa.getContatos()) {
                    contatoRepository.delete(contato);
                }

                pessoaRepository.delete(pessoa);

                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



    public void respostaChamada(String Menssagem, Request request, GenericResponse response, PessoaModel pessoaModel, String detalhes, Integer status) {
        request.getRequestObjects().put("pessoa", pessoaModel);
        response.setRequest(request);
        response.setMessage(Menssagem);
        response.setStatus(status);
        response.setTimestamp(new Timestamp(System.currentTimeMillis()));
        response.setDetails(detalhes);

    }


}
