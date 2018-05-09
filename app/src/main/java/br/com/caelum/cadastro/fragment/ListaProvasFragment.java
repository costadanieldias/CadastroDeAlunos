package br.com.caelum.cadastro.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import br.com.caelum.cadastro.R;
import br.com.caelum.cadastro.activity.ProvasActivity;
import br.com.caelum.cadastro.vo.Prova;

/**
 * Created by android5497 on 10/09/15.
 */
public class ListaProvasFragment extends Fragment {
    private ListView listViewProvas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layoutProvas = inflater.inflate(R.layout.fragment_lista_provas, container, false);
        this.listViewProvas = (ListView) layoutProvas.findViewById(R.id.lista_provas_listview);

        Prova prova1 = new Prova("20/06/2015", "Matemática");
        prova1.setTopicos(Arrays.asList("Álgebra Linear", "Cálculo", "Estatística"));

        Prova prova2 = new Prova("13/07/2015", "Portugues");
        prova2.setTopicos(Arrays.asList("Complemento Nominal", "Análise Sintática", "Oracoes subordinadas"));

        List<Prova> provas = Arrays.asList(prova1, prova2);

        this.listViewProvas.setAdapter(new ArrayAdapter<Prova>(getActivity(), android.R.layout.simple_list_item_1, provas));

        this.listViewProvas.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Prova provaSelecionada = (Prova) parent.getItemAtPosition(position);
//                Toast.makeText(getActivity(), "Prova selecionada: " + provaSelecionada, Toast.LENGTH_LONG).show();
//                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                ProvasActivity calendarioProvas = (ProvasActivity) getActivity();
                calendarioProvas.selecionaProva(provaSelecionada);
            }
        });

        return layoutProvas;
    }



}
