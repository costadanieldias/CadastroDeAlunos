package br.com.caelum.cadastro.fragment;

import android.location.Address;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import br.com.caelum.cadastro.dao.AlunoDAO;
import br.com.caelum.cadastro.util.Localizador;
import br.com.caelum.cadastro.vo.Aluno;

/**
 * Created by android5497 on 11/09/15.
 */
public class MapaFragment extends SupportMapFragment {
    Localizador localizador;
    LatLng local;
    MarkerOptions marcador;

    @Override
    public void onResume() {
        super.onResume();
        localizador = new Localizador(getActivity());
        local = localizador.getCoordenada("Rua Vergueiro");
        marcador = new MarkerOptions();
        marcador.position(local)
                .title("CAELUM")
                .snippet("Vergueiro");
        getMap().addMarker(marcador);
        Log.i("MAPA", "Coordenadas da Caelum: " + local);

//      Cria marcadores para os alunos
        AlunoDAO dao = new AlunoDAO(getActivity());
        for(Aluno aluno : dao.getLista()) {
            local = localizador.getCoordenada(aluno.getEndereco());
            marcador = new MarkerOptions();
            marcador.position(local)
                    .title(aluno.getNome())
                    .snippet(String.valueOf(aluno.getNota()));
            getMap().addMarker(marcador);
        }

        if(local != null) {
            centralizaNo(local);
        }

    }

    private void centralizaNo(LatLng local) {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(local, 13));
    }
}
