package basket.services.rest;

import basket.repos.RepositoryException;
import basket.repos.MeciDataBaseRepository;
import basket.model.domain.Meci;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@ComponentScan("basket")
@CrossOrigin
@RestController
@RequestMapping("/basket/meciuri")
public class MeciController {
    @Autowired
    private MeciDataBaseRepository baseRepository;


    @RequestMapping( method=RequestMethod.GET)
    public Meci[] getAll() {
        List<Meci> list = StreamSupport.stream(baseRepository.findAll().spliterator(), false).collect(Collectors.toList());
        Meci[] meciuri = new Meci[list.size()];
        list.toArray(meciuri);
        return meciuri;
    }


    @RequestMapping(value = "/{meci}", method = RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable String meci) {
        Meci meciGasit = baseRepository.findOne(meci);
        if (meciGasit == null)
            return new ResponseEntity<String>("Meciul nu a putut fi gasit!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Meci>(meciGasit, HttpStatus.OK);
    }


    @RequestMapping(method = RequestMethod.POST)
    public Meci create(@RequestBody Meci meci){
            baseRepository.save(meci);
            return meci;
    }


    @RequestMapping(value = "/{meci}", method = RequestMethod.PUT)
    public Meci update(@RequestBody Meci meci) {
            System.out.println("Modificare meci ...");
             baseRepository.update(meci);
             return meci;

    }


    @RequestMapping(value="/{meci}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable String meci){
        System.out.println("Stergere meci ... "+meci);
        try {
            baseRepository.delete(meci);
            return new ResponseEntity<Meci>(HttpStatus.OK);
        }catch (RepositoryException ex){
            System.out.println("Exceptie in Controller la stergerea meciului.");
            return new ResponseEntity<String>(ex.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping("/{meci}/tip")
    public String tip(@PathVariable String meci){
        Meci result= baseRepository.findOne(meci);
        System.out.println("Rezultat Tip ..."+result);
        return result.getTip().toString();
    }



    @ExceptionHandler(RepositoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(RepositoryException e) {
        return e.getMessage();
    }
}
