package br.ce.wcaquino.dao;

import br.ce.wcaquino.entidades.Locacao;

import java.util.List;

public class LocacaoDAOFake implements LocacaoDAO{

    public void salvar(Locacao locacao) {

    }

    @Override
    public List<Locacao> obterLocacoesPendentes() {
        return null;
    }
}
