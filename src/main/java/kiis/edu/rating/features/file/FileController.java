package kiis.edu.rating.features.file;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/file")
public class FileController {
    private final FilesService filesService;

    @GetMapping("/")
    public List<FileInfo> getListFiles() {
        return filesService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class,
                            "getFile",
                            path.getFileName().toString())
                    .build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());
    }

    @GetMapping("/{filename:.+}")
    public Resource getFile(@PathVariable String filename) {
        return filesService.load(filename);
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam MultipartFile file) {
        filesService.save(file);
        return "Uploaded the file successfully: " + file.getOriginalFilename();
    }
}
