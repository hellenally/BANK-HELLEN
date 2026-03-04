package com.bank.bankhellen.service;

import com.bank.bankhellen.entity.Nasabah;
import com.bank.bankhellen.model.dto.UpdateNasabahRequest;
import com.bank.bankhellen.repository.NasabahRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateNasabahService {
    @Autowired
    private NasabahRepository repository;

    public Nasabah reactivate(String nomorKtp) throws Exception {
        Nasabah n = repository.findByNomorKtp(nomorKtp).orElseThrow(() -> new Exception("Nasabah tidak ditemukan!"));
        if (n.getStatus() == 1) throw new Exception("Nasabah sudah aktif!");
        n.setStatus(1);
        return repository.save(n);
    }

    public Nasabah execute(String nomorKtp, UpdateNasabahRequest r) {
        return repository.findByNomorKtp(nomorKtp).map(n -> {
            if (r.getNamaLengkap() != null && !r.getNamaLengkap().isEmpty()) n.setNamaLengkap(r.getNamaLengkap());
            if (r.getAlamat() != null && !r.getAlamat().isEmpty()) n.setAlamat(r.getAlamat());
            if (r.getNomorHandphone() != null && !r.getNomorHandphone().isEmpty()) n.setNomorHandphone(r.getNomorHandphone());
            if (r.getTempatLahir() != null && !r.getTempatLahir().isEmpty()) n.setTempatLahir(r.getTempatLahir());
            if (r.getTanggalLahir() != null) n.setTanggalLahir(r.getTanggalLahir());

            return repository.save(n);
        }).orElse(null);
    }
}