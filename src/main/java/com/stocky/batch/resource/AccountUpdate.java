package com.stocky.batch.resource;

public class AccountUpdate {
//	private static Connection conn = ConnectionUtil.getConnection(Constants.STOCKYDATABASE);
//	private static AccountUpdate _instance;
//	private AccountUpdate(){};
//
//	public AccountUpdate getInstance(){
//        if (_instance == null) {
//            synchronized (AccountUpdate.class) {
//                _instance = new AccountUpdate();
//            }
//        }
//        return _instance;
//    }
//	
//	public void run(){
//        try {
//            String query = "select distinct(userId) from user";
//            PreparedStatement ps = conn.prepareStatement(query);
//            ResultSet rs = ps.executeQuery();
//            ResultSetMapper<User> resultSetMapper = new ResultSetMapper<User>();
//            List<User> userList = resultSetMapper.mapResultSetToObjects(rs, User.class);
//            for(User u : userList){
//                Account account = AccountUtil.findExistingAccount(conn, u.getUserId());
//                getAndUpdateCurrentPortfolioValue(u, 0, account);
//            }
//            conn.commit();
//            conn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void getAndUpdateCurrentPortfolioValue(User u, double portfolioValue, Account account) throws SQLException, ParseException {
//        Statement st = conn.createStatement();
//        String query = "select * from portfolio where userId='"+u.getUserId()+"'";
//        PreparedStatement ps = conn.prepareStatement(query);
//        ResultSet rs = ps.executeQuery();
//        ResultSetMapper<Portfolio> resultSetMapper = new ResultSetMapper<Portfolio>();
//        List<Portfolio> portfolioList = resultSetMapper.mapResultSetToObjects(rs, Portfolio.class);
//        Map<String, Portfolio> portfolioMap = new HashMap<String, Portfolio>();
//        if(!CollectionUtils.isEmpty(portfolioList)){
//            portfolioMap = portfolioList.stream().collect(
//                    Collectors.toMap(Portfolio::getStockId, p -> p));
//        }
//
//        List<Map<String, String>> stockLatest = CurrentPriceUtil.getStockPrices(new ArrayList<>(portfolioMap.keySet()));
//
//        for(Map<String, String> m : stockLatest){
//            Map.Entry<String, String> e = m.entrySet().iterator().next();
//            portfolioValue += portfolioMap.get(e.getKey()).getQuantity() * Double.valueOf(e.getValue());
//        }
//
//        //Updating the account value every 10 mins
//        AccountUtil.updateAccount(conn, u.getUserId(), account, 0, Utility.getCurrentDate(), portfolioMap);
//    }
}