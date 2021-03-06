package com.cs122b.project.Fabflix.controller;

import com.cs122b.project.Fabflix.Response.*;
import com.cs122b.project.Fabflix.Service.CustomerService;
import com.cs122b.project.Fabflix.Service.MovieService;
import com.cs122b.project.Fabflix.model.CartItem;
import com.cs122b.project.Fabflix.session.CartSession;
import com.cs122b.project.Fabflix.utils.RecaptchaVerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class MovieController {

    @Autowired
    private MovieService movieService;

    private CustomerService customerService;

    @GetMapping("/movie")
    public MovieResponse SingleMovie(@RequestParam("movieId") String movieId) {

        return movieService.movieDetail(movieId);
    }

    @GetMapping("/star")
    public StarResponse SingleStar(@RequestParam("starId") String starId) {
        return movieService.starDetail(starId);
    }

    @PostMapping("/login")
    public BaseResponse CustomerLogin(@RequestParam("email")String email, @RequestParam("password") String password,
                                      @RequestParam("g-recaptcha-response") String recap,
                                      HttpSession session) throws Exception {
        try {
            RecaptchaVerifyUtils.verify(recap);
        } catch (Exception e) {
            BaseResponse response=new BaseResponse(1);
            response.setData("Recaptcha Error!");
            return response;
        }
        BaseResponse response = movieService.login(email,password);
        if (response.getMessage() == 0){
            session.setAttribute(session.getId(),response);
            session.setAttribute("cart",new CartSession());
        }
        return response;
    }

    // android login without recap
    @PostMapping("/android_login")
    public BaseResponse AndroidLogin(@RequestParam("username")String email, @RequestParam("password") String password,
                                      HttpSession session) throws Exception {

        BaseResponse response = movieService.login(email,password);
        System.out.println(email+" "+password);
        if (response.getMessage() == 0){
            session.setAttribute(session.getId(),response);
            //session.setAttribute("cart",new CartSession());
        }

        System.out.println(response);
        return response;
    }

    @RequestMapping(value = "/user")
    public BaseResponse getUserName(HttpSession session){


        return  (BaseResponse) session.getAttribute(session.getId());
    }


    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){
        session.removeAttribute(session.getId());
        return "{\"message\":-1,\"data\":\"Logout Success!\"}";
    }

    //Search movie: with substring matching: /api/search?title=t&year=year&director=d&star=s&page=1&pagesize=20&sort=title&order=asc
    @GetMapping("/search")
    public SearchResponse Search(@RequestParam("title") String title, @RequestParam("year") String year,
                                 @RequestParam("director") String director, @RequestParam("star") String starName,
                                 @RequestParam("page") int page, @RequestParam("pagesize") int pagesize,
                                 @RequestParam("sort") String sort, @RequestParam("order") String order,@RequestHeader Map<String, String> headers) {
        return movieService.search(title, year, director, starName, page, pagesize, sort, order, headers);
    }

    //search movies in a genre:
    @GetMapping("/list")
    public SearchResponse search_in_genre(@RequestParam("genre") String genre, @RequestParam("page") int page,
                                        @RequestParam("pagesize") int pagesize, @RequestParam("sort") String sort,
                                        @RequestParam("order") String order){
        return movieService.genreList(genre, page, pagesize, sort, order);
    }

    //search movies start with alphabet:
    @GetMapping("/listalpha")
    public SearchResponse search_in_alpha(@RequestParam("alphabet") String alphabet, @RequestParam("page") int page,
                                        @RequestParam("pagesize") int pagesize, @RequestParam("sort") String sort,
                                        @RequestParam("order") String order){
        return movieService.alphaList(alphabet, page, pagesize, sort, order);
    }

    //List all movie genre sort alphabetical: /api/genres
    @GetMapping("/genres")
    public ListGenResponse list_in_alpha(){
        return movieService.genlist();
    }

    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("movieId") String movieId, @RequestParam("movieTitle") String movieTitle,
                          HttpSession session){
        CartSession cs = (CartSession) session.getAttribute("cart");
        cs.addItemToCart(movieId, movieTitle, 1);
        //System.out.println(movieId);
        return "{\"message\":0,\"data\":\"Add Success!\"}";
    }

    @PostMapping("/cart/update")
    public CartResponse updateCart(@RequestBody ArrayList<CartItem> cartList, HttpSession session){
        System.out.println(cartList.toString());
        CartSession cs = (CartSession) session.getAttribute("cart");
        cs.removeAllItemsFromCart();
        cs.addListToCart(cartList);
        CartResponse cr = new CartResponse(0, cartList);
        return cr;
    }

    @GetMapping("/cart/show")
    public CartResponse showCart(HttpSession session){
        CartSession cs = (CartSession) session.getAttribute("cart");
        ArrayList<CartItem> cartItems = cs.getCartItems();
        CartResponse cr = new CartResponse(0, cartItems);
        return cr;
    }

    @PostMapping("cart/checkout")
    public CheckoutResponse checkout(@RequestParam("first") String firstname, @RequestParam("last") String lastname,
                         @RequestParam("number") String number, @RequestParam("expire") String expire,
                                     @RequestParam("userId") String userId, HttpSession session){
        return movieService.checkoutService(firstname, lastname, number, expire, userId, session);
    }

    @PostMapping("/dash/login")
    public BaseResponse AdminLogin(@RequestParam("email")String email, @RequestParam("password") String password,
                                      @RequestParam("g-recaptcha-response") String recap,
                                      HttpSession session) throws Exception {

        try {
            RecaptchaVerifyUtils.verify(recap);
        } catch (Exception e) {
            BaseResponse response=new BaseResponse(1);
            response.setData("Recaptcha Error!");
            return response;
        }
        BaseResponse response = movieService.adminlogin(email,password);
        if (response.getMessage() == 0){
            session.setAttribute(session.getId(),"admin");
            session.setAttribute("admin",response.getData());
        }
        return response;
    }


    @GetMapping("/dash/show")
    public AdminResponse showDash(HttpSession session) {
        AdminResponse ar = new AdminResponse();
        String role=(String)session.getAttribute(session.getId());
        if(!role.equals("admin")){

            ar.setMessage(-1);
            return ar;
        }
        ar.setMessage(0);
        DashData dt=new DashData();
        dt.setAdmin((String)session.getAttribute("admin"));
        ArrayList<Table> tables=movieService.getTableInfo();
        dt.setTables(tables);
        ar.setData(dt);

        return ar;
    }

    @PostMapping("/dash/addMovie")
    public BaseResponse addMovie(@RequestParam("title") String title, @RequestParam("year") String year,
                                 @RequestParam("director") String director, @RequestParam("star") String starName,
                                 @RequestParam("genre") String genre){

        return movieService.addMovie(title, year, director, starName, genre);
    }

    @PostMapping("/dash/addStar")
    public BaseResponse addStar(@RequestParam("name") String name, @RequestParam("birth") String birth){

        return movieService.addStar(name, birth);
    }

    @GetMapping("/fsearch")
    public BaseResponse fullTextSearch(@RequestParam("text") String text) {
        return movieService.fullSearch(text);
    }

}
