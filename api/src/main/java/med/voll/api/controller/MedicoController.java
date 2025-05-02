package med.voll.api.controller;

import med.voll.api.endereco.Endereco;
import med.voll.api.medico.DadosCadastroMedico;
import med.voll.api.medico.Medico;
import med.voll.api.medico.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity<?> cadastrar(@RequestBody DadosCadastroMedico dados){
        List<Medico> medicos = repository.findAll();

        if(medicos.stream().anyMatch(medic -> medic.getCrm().equals(dados.crm()))) {
            return new ResponseEntity<>("Já existe um médico com esse crm", HttpStatus.CONFLICT);
        }

        if(medicos.stream().anyMatch(medic -> medic.getEmail().equals(dados.email()))) {
            return new ResponseEntity<>("Já existe um médico com esse email", HttpStatus.CONFLICT);
        }

        var medico = Medico.builder()
                .nome(dados.nome())
                .email(dados.email())
                .crm(dados.crm())
                .especialidade(dados.especialidade())
                .endereco(Endereco.builder()
                        .logradouro(dados.endereco().logradouro())
                        .bairro(dados.endereco().bairro())
                        .cep(dados.endereco().cep())
                        .cidade(dados.endereco().cidade())
                        .uf(dados.endereco().uf())
                        .numero(dados.endereco().numero())
                        .complemento(dados.endereco().complemento())
                        .build())
                .build();

        var novoMedico = repository.save(medico);

        return new ResponseEntity<>(novoMedico, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public Medico obterDetalhesDoMedico(@PathVariable("id") Long id) {
        var medicoOptional = repository.findById(id);
        return medicoOptional.orElse(null);
    }
}
