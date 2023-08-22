package PessoaControllerTest;

import com.testeEloTech.Controller.PessoaController;
import com.testeEloTech.Model.PessoaModel;
import com.testeEloTech.Repository.PessoaDao;
import com.testeEloTech.Service.ContatoService;
import com.testeEloTech.Service.PessoaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PessoaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private ContatoService contatoService;

    @InjectMocks
    private PessoaController pessoaController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pessoaController).build();

        // Configurar o contexto do request
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testCadastrarPessoaSucesso() throws Exception {
        // Configurar comportamento esperado do serviço mockado
        when(pessoaService.verificaCamposPessoa(any(PessoaModel.class))).thenReturn("Pessoa Criada com Sucesso.");

        String requestBody = "{\n" +
                "  \"nome\": \"ro\",\n" +
                "  \"cpf\": \"981.288.550-18\",\n" +
                "  \"dataNascimento\": \"2023-01-01\",\n" +
                "  \"contatos\": [\n" +
                "    {\n" +
                "      \"nome\": \"ll\",\n" +
                "      \"telefone\": \"123-456-7890\",\n" +
                "      \"email\": \"contato1@example.com\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"nome\": \"pp\",\n" +
                "      \"telefone\": \"987-654-3210\",\n" +
                "      \"email\": \"contato2@exampl.com\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";  // Forneça o corpo JSON de uma pessoa válida

        mockMvc.perform(post("/api/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json("Boa")); // Forneça o conteúdo JSON da resposta esperada
    }

    // Outros testes para os métodos do controller
}
