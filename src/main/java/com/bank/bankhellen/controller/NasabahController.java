package com.bank.bankhellen.controller;

import com.bank.bankhellen.entity.Nasabah;
import com.bank.bankhellen.model.dto.*;
import com.bank.bankhellen.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nasabah")
public class NasabahController {

    private final CreateNasabahService createService;
    private final DeleteNasabahService deleteService;
    private final ReadNasabahService readService;
    private final UpdateNasabahService updateService;

//    public NasabahController(CreateNasabahService createService,
//                             DeleteNasabahService deleteService,
//                             ReadNasabahService readService,
//                             UpdateNasabahService updateService) {
//        this.createService = createService;
//        this.deleteService = deleteService;
//        this.readService = readService;
//        this.updateService = updateService;
//    }

    @PostMapping("/daftar")
    public ResponseEntity<WebResponse<Object>> create(@Valid @RequestBody CreateNasabahRequest request) throws Exception {
        Nasabah result = createService.execute(request);
        return ResponseEntity.status(201).body(WebResponse.builder()
                .responseCode(201).status("OK").message("Sukses").data(result).build());
    }


    @GetMapping("/semua")
    public ResponseEntity<WebResponse<List<Nasabah>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        Page<Nasabah> resultPage = readService.findAll(page, size);

        return ResponseEntity.ok(WebResponse.<List<Nasabah>>builder()
                .responseCode(200)
                .status("OK")
                .message("Berhasil mengambil data")
                .data(resultPage.getContent())
                        .paging(PagingResponse.builder()
                                .currentPage(resultPage.getNumber())
                                .totalPage(resultPage.getTotalPages())
                                .size(resultPage.getSize())
                                .totallElement(resultPage.getTotalElements())
                                .numberOfElements(resultPage.getNumberOfElements())
                                .build())
                .build());
    }

    @GetMapping("/cari/{nomorKtp}")
    public ResponseEntity<WebResponse<Object>> getByKtp(@PathVariable String nomorKtp) throws Exception {
        Nasabah result = readService.findByKtp(nomorKtp);
        if (result == null) throw new Exception("404:Nasabah dengan KTP tersebut tidak ditemukan");

        return ResponseEntity.ok(WebResponse.builder()
                .responseCode(200).status("OK").message("Data ditemukan").data(result).build());
    }

    @PutMapping("/update/{nomorKtp}")
    public ResponseEntity<WebResponse<Object>> update(@PathVariable String nomorKtp, @RequestBody UpdateNasabahRequest request) throws Exception {
        Nasabah result = updateService.execute(nomorKtp, request);
        if (result == null) throw new Exception("404:ID Nasabah tidak ditemukan");

        return ResponseEntity.ok(WebResponse.builder()
                .responseCode(200).status("OK").message("Update berhasil").data(result).build());
    }

    @DeleteMapping("/hapus/{nomorKtp}")
    public ResponseEntity<WebResponse<Object>> delete(@PathVariable String nomorKtp) throws Exception {
        deleteService.execute(nomorKtp);
        return ResponseEntity.ok(WebResponse.builder().responseCode(200).status("OK").message("Berhasil").build());
    }

    @PutMapping("/aktifkan/{nomorKtp}")
    public ResponseEntity<WebResponse<Object>> reactivate(@PathVariable String nomorKtp) throws Exception {
        Nasabah result = updateService.reactivate(nomorKtp);
        return ResponseEntity.ok(WebResponse.builder().responseCode(200).status("OK").data(result).build());
    }
}