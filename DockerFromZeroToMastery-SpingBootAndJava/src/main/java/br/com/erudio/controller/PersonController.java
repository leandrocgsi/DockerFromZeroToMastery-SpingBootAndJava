package br.com.erudio.controller;
 
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.services.PersonServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@Api(value = "PersonEndpoint", description = "REST API for Person", tags = { "PersonEndpoint" })
@RestController
@RequestMapping("/api/person/v1")
public class PersonController {
     
    @Autowired
    private PersonServices service;
      
	@Autowired
	private PagedResourcesAssembler<PersonVO> assembler;
     
    @ApiOperation(value = "Find a specific person by your ID" )
    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO get(@PathVariable(value = "id") Long id){
        PersonVO personVO = service.findById(id);
        personVO.add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
        return personVO;
    }
    
    @ApiOperation(value = "Find all people" ) 
    @RequestMapping(method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "30") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction){
    	
    	var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    	
    	Pageable pageableRequest = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
    	
    	
    	Page<PersonVO> persons = service.findAll(pageableRequest);


    	persons.forEach(p -> p.add(
				linkTo(methodOn(PersonController.class).get(p.getKey())).withSelfRel()
			)
		);
        PagedResources<?> resources = assembler.toResource(persons);

        return ResponseEntity.ok(resources);
    }
    
    @ApiOperation(value = "Find a specific person by name" ) 
    @RequestMapping(value = "/findPersonByName/{firstName}",
    method = RequestMethod.GET,
	produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<?> findPersonByName(
    		@PathVariable(value = "firstName") String firstName,
    		@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "30") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction){
    	
    	var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    	
    	Pageable pageableRequest = PageRequest.of(page, limit, Sort.by(sortDirection, "firstName"));
    	
    	
    	Page<PersonVO> persons = service.findPersonByName(firstName, pageableRequest);

    	persons.forEach(p -> p.add(
    				linkTo(methodOn(PersonController.class).get(p.getKey())).withSelfRel()
				)
			);
        PagedResources<?> resources = assembler.toResource(persons);

        return ResponseEntity.ok(resources);
    }
    
    @ApiOperation(value = "Create a new person") 
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO create(@RequestBody PersonVO person){
    	PersonVO personVO = service.create(person);
        personVO.add(linkTo(methodOn(PersonController.class).get(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Update a specific person")
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public PersonVO update(@RequestBody PersonVO person){
    	PersonVO personVO = service.update(person);
        personVO.add(linkTo(methodOn(PersonController.class).get(personVO.getKey())).withSelfRel());
        return personVO;
    }
    
    @ApiOperation(value = "Set a specific person by to disabled")
    @RequestMapping(value = "/{id}",
    method = RequestMethod.PATCH)
    public PersonVO patch(@PathVariable(value = "id") Long id){
    	PersonVO personVO = service.disablePerson(id);
        personVO.add(linkTo(methodOn(PersonController.class).get(personVO.getKey())).withSelfRel());
        return personVO;
    }

    @ApiOperation(value = "Delete a specific person by your ID")
    @RequestMapping(value = "/{id}",
    method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }

}
