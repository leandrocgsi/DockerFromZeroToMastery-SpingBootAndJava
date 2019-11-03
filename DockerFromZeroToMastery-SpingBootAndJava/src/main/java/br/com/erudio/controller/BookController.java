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

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.services.BookServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
 
@Api(value = "BookEndpoint") 
@RestController
@RequestMapping("/api/book/v1")
public class BookController {
     
    @Autowired
    private BookServices services;
    
	@Autowired
	private PagedResourcesAssembler<BookVO> assembler;
	
    @ApiOperation(value = "Find a specific book by your ID" )
    @RequestMapping(value = "/{id}",
    method = RequestMethod.GET, 
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO get(@PathVariable(value = "id") Long id){
        BookVO bookVO = services.findById(id);
        bookVO.add(linkTo(methodOn(BookController.class).get(id)).withSelfRel());
        return bookVO;
    }
    
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "30") int limit,
            @RequestParam(value = "direction", defaultValue = "asc") String direction){
    	
    	var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
    	
    	Pageable pageableRequest = PageRequest.of(page, limit, Sort.by(sortDirection, "title"));
    	
    	
    	Page<BookVO> books = services.findAll(pageableRequest);


    	books
    		.stream()
    		.forEach(p -> p.add(
    				linkTo(methodOn(BookController.class).get(p.getKey())).withSelfRel()
				)
			);
        PagedResources<?> resources = assembler.toResource(books);

        return ResponseEntity.ok(resources);
    }
    
    @ApiOperation(value = "Create a new book") 
    @RequestMapping(method = RequestMethod.POST,
    consumes = { "application/json", "application/xml", "application/x-yaml" },
    produces = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO create(@RequestBody BookVO book){
    	BookVO bookVO = services.create(book);
        bookVO.add(linkTo(methodOn(BookController.class).get(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Update a specific book")
    @RequestMapping(method = RequestMethod.PUT,
    consumes = { "application/json", "application/xml", "application/x-yaml" })
    public BookVO update(@RequestBody BookVO book){
    	BookVO bookVO = services.update(book);
        bookVO.add(linkTo(methodOn(BookController.class).get(bookVO.getKey())).withSelfRel());
        return bookVO;
    }

    @ApiOperation(value = "Delete a specific book by your ID")
    @RequestMapping(value = "/{id}",
    method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id){
        services.delete(id);
        return ResponseEntity.ok().build();
    }
}
