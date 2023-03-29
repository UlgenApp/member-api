package tr.edu.ku.ulgen.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.edu.ku.ulgen.client.ProducerClient;
import tr.edu.ku.ulgen.dto.UlgenDto;

@RestController
@RequestMapping("/api/v1/producer")
@AllArgsConstructor
public class DataProducerController {
    private ProducerClient producerClient;

    @PostMapping("/produce/{uid}")
    public ResponseEntity<?> authenticate(@PathVariable("uid") String uid, @RequestBody UlgenDto request) {
        return ResponseEntity.ok(producerClient.produceData(uid, request));
    }
}
