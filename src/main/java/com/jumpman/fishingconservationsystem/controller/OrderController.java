package com.jumpman.fishingconservationsystem.controller;


import com.jumpman.fishingconservationsystem.entity.*;
import com.jumpman.fishingconservationsystem.mails.EmailSenderService;
import com.jumpman.fishingconservationsystem.service.*;
import com.jumpman.fishingconservationsystem.sms.SmsRequest;
import com.jumpman.fishingconservationsystem.sms.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Controller
public class OrderController {
    private final OrderService orderService;
    private final FishService fishService;
    private final UserService userService;
    private final FishStoreService fishStoreService;
    private final CooperativeService cooperativeService;
    @Autowired
    private EmailSenderService service;
    @Autowired
    private SmsService smsService;
    private List<Order> userOrders;
    public OrderController(OrderService orderService,FishService fishService,
                           UserService userService,
                           FishStoreService fishStoreService,CooperativeService cooperativeService){
        this.fishService=fishService;
        this.orderService=orderService;
        this.userService=userService;
        this.fishStoreService=fishStoreService;
        this.cooperativeService=cooperativeService;
    }
    //get orders form
    @GetMapping("/fishing/orders")
    public String getOrdersForm(Model model){

        model.addAttribute("orders",orderService.viewOrder());
        return "orders";
    }

    //get orders form for the cooperative
    @PostMapping("/fishing/coop_orders")
    public String getOrderForCooperativeForm(@RequestParam("coopUsername")String coopUsername, Model model){
        Cooperative cooperative=cooperativeService.findByUsername(coopUsername);
        model.addAttribute("orders",orderService.getOrdersByCooperative(cooperative));
        model.addAttribute("cooperative",cooperative);
        return "coop_orders";
    }

    //get orders form for the user
    @GetMapping("/fishing/user_orders")
    public String getOrderForUserForm(HttpServletRequest req, HttpServletResponse resp, Model model) throws IOException, ServletException {
        String action=req.getParameter("action");
        String username=req.getParameter("username");

        if(action==null){
            return"user_orders";
        }else {
            if (action.equalsIgnoreCase("buy") && username!=null){
                addItemInCart(req,resp, username,model);
            }

        }
        return "user_orders";
    }
    @GetMapping("/fishing/revert/user_orders")
    public String removeItemFromCart(HttpServletRequest req, HttpServletResponse resp, Model model) {
        String action=req.getParameter("action");
        String username=req.getParameter("username");
        String orderId =req.getParameter("order");
        Order order=null;
        if (action==null){
            return "user_orders";
        }else {
            if (action.equalsIgnoreCase("revert") && username!=null){
                for (int i=0;i<userOrders.size();i++){
                    if (userOrders.get(i).getOrderId()==Integer.parseInt(orderId)){
                        order=userOrders.get(i);
                        userOrders.remove(order);
                        return "user_orders";
                    }
                }


            }

        }
        return "user_orders";
    }

    //user proceed with ordering
    @PostMapping("/fishing/order/user_proceed")
    public String orderPayment(HttpServletRequest req,Model model) throws IOException {
        orderService.saveOrders(userOrders);
        for(Order order:userOrders){
            requestingOrder(order,model);
        }

//
        //we should return a sms confirmation of products bought
        String randomOtp=randomString();
        String phoneNumber="+250789394104";
        String twilioMsg="Hello "+userOrders.get(0).getUser().getFirstName()+" Welcome to Fish Farming Thanks for buying with us \n \n your OTP number for your product: "+randomOtp+" ";
        triggerTheEmailMsg("jumpmantest79@gmail.com",userOrders.get(0).getUser().getFirstName(),randomOtp);
        SmsRequest smsRequest=new SmsRequest(phoneNumber,twilioMsg);
        smsService.sendSms(smsRequest);

        model.addAttribute("username",userOrders.get(0).getUser().getFirstName());
        model.addAttribute("successfully","your order made successfully");
        List<Order> list=new ArrayList<>();
        HttpSession session=req.getSession();

        session.setAttribute("orders",list);
        return"user_orders";
    }
//create a requesting order
    private void requestingOrder( Order order,Model model){
        Double orderNumber=order.getFishNumberOrdered();
        String fishName=order.getFishType();
        List<FishStore> randomFishes=new ArrayList<>();
        List<FishStore> fishes=fishStoreService.getFishesByName(fishName);
        List<FishStore> fishStoreList=new ArrayList<>(fishes);
        //picking fishes randomly from the db
        //so as all on each order at least one or more cooperative shall sell
        SecureRandom rand=new SecureRandom();
        for(int i=0;i<Math.min(orderNumber,fishes.size());i++){
            randomFishes.add(fishStoreList.remove(rand.nextInt(fishStoreList.size())));
        }
                //updating the store via created random list
                for(int i=0;i<randomFishes.size();i++){
                    if( !randomFishes.get(i).getFish().getName().contains(fishName)){
                        model.addAttribute("notFound",fishName+" not available");
                    }else {
                    if(randomFishes.get(i).getRemainedFish()>=orderNumber){
                        Double remained=randomFishes.get(i).getRemainedFish()-orderNumber;
                        randomFishes.get(i).setRemainedFish(remained);
                        Double soldedFish=randomFishes.get(i).getSoldFish()+orderNumber;
                        randomFishes.get(i).setSoldFish(soldedFish);
                        randomFishes.get(i).setLastDayFishSold(LocalDate.now());
                        randomFishes.get(i).setAmount(soldedFish* randomFishes.get(i).getFish().getPrice());

                        fishStoreService.updateStore(randomFishes.get(i));
                        break;
                    }

                    if(orderNumber!=0&&randomFishes.get(i).getRemainedFish()>=1){
                        Double remained=randomFishes.get(i).getRemainedFish()-1;
                        randomFishes.get(i).setRemainedFish(remained);
                        Double soldedFish=randomFishes.get(i).getSoldFish()+1;
                        randomFishes.get(i).setSoldFish(soldedFish);
                        randomFishes.get(i).setLastDayFishSold(LocalDate.now());
                        randomFishes.get(i).setAmount(soldedFish* randomFishes.get(i).getFish().getPrice());
                        fishStoreService.updateStore(randomFishes.get(i));
                        orderNumber --;
                    }
                }
        }



    }

    //get refound form to the admin page
    @GetMapping("/fishing/refound")
    public String getRefoundForm(Model model){
        return "refound";

    }

    private void addItemInCart(HttpServletRequest req, HttpServletResponse resp,String username,Model model) throws ServletException, IOException {
        String orderId =req.getParameter("order");
        HttpSession session=req.getSession();
            Fish fish=fishService.getSingleFishById(Integer.parseInt(orderId));
            //getting a cooperative that uploaded the fish
            //in order to put it on order a client made
            FishStore fishStore=fishStoreService.findCooperativeByFish(fish.getId());
            if (fishStore==null){
                model.addAttribute("noFish","this "+fish.getName()+"not in store now");

                resp.sendRedirect("/fishing/user_page?username="+username);
                return;
            }
            User user=userService.findByUsername(username);
        if(session.getAttribute("orders")==null){
            ArrayList<Order> orders=new ArrayList<Order>();
            Order order=Order.builder().orderId(fish.getId())
                            .fishType(fish.getName())
                            .amount(fish.getPrice())
                            .fishNumberOrdered(1.0)
                            .dateOrderMade(LocalDate.now())
                            .paymentMethod("MOMO")
                            .user(user)
                            .cooperative(fishStore.getCooperative())
                            .build();
//            adding order made in a list
            orders.add(order);
            session.setAttribute("orders",orders);
            session.setAttribute("user",user);
            userOrders=orders;
            resp.sendRedirect("/fishing/user_orders");
        }else {
            ArrayList<Order> orders=(ArrayList<Order>)session.getAttribute("orders");
            int index=isCartExist(orderId, orders);
            if(index==-1){
                Order order=Order.builder().orderId(fish.getId())
                        .fishType(fish.getName())
                        .amount(fish.getPrice())
                        .fishNumberOrdered(1.0)
                        .dateOrderMade(LocalDate.now())
                        .paymentMethod("MOMO")
                        .user(user)
                        .cooperative(fishStore.getCooperative())
                        .build();
//                adding order made in a list
                orders.add(order);
            }else {
                double quantity=orders.get(index).getFishNumberOrdered() +1;
                orders.get(index).setFishNumberOrdered(quantity);
            }
            session.setAttribute("orders",orders);
            session.setAttribute("user",user);
            userOrders=orders;
            resp.sendRedirect("/fishing/user_orders");
        }
    }
    private int isCartExist(String code,ArrayList<Order> orders){
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getOrderId()==Integer.parseInt(code)){
                return i;
            }
        }
        return -1;
    }

    private void triggerTheEmailMsg(String email,String user,String otp){
        service.sendCommunicationEmail(
                email,
                "Hello "+user+" Welcome to Fish Farming Thanks for buying with us \n \n your OTP number for your product: "+otp+" ",
                "Fish Farming Orders"
        );
    }

    private  String randomString() {

        String characters = "0123456789";
        String str = "";
        String createdEmplCode="OTP-";
        char[] mynewCharacters = characters.toCharArray();
        Integer generatedCodeLength =6;
        for (int i = 0; i < generatedCodeLength; i++) {
            int index = (int) (Math.random() *10);
            String newString = characters.substring(index, characters.length() - 1);
            str += mynewCharacters[newString.length()];
        }
        return createdEmplCode.concat(str);
    }

}
