/*
 * StrategyData.java
 *
 * Created on July 23, 2002, 8:56 AM
 */

package com.argus.financials.strategy;

/**
 * 
 * @author valeri chibaev
 * @version
 */

public class StrategyData implements java.io.Serializable {

    public Client client;

    /** Creates new StrategyData */
    public StrategyData() {
        client = new Client();
    }

    public class Client implements java.io.Serializable {

        public Person person = new Person();

        public Asset asset = new Asset();

        public AssetCash assetCash = new AssetCash();

        public AssetInvestment assetInvestment = new AssetInvestment();

        public AssetPersonal assetPersonal = new AssetPersonal();

        public AssetSuperannuation assetSuperannuation = new AssetSuperannuation();

        public Strategy strategy = new Strategy();

        public class Person implements java.io.Serializable {

            public String FullName; // ClientView.Person.FullName

        }

        /*
         * FINANCIAL (usually client)
         */
        public class Asset implements java.io.Serializable {

            public String TotalAmount;

        }

        public class AssetCash implements java.io.Serializable {

            public String TotalAmount;

            public java.util.ArrayList Items = new java.util.ArrayList();

            public class Item implements java.io.Serializable {
                public String Amount;

                public String AccountNumber;

                public String FinancialType;

                public String FinancialCode;

                public String FinancialDesc;

                public String Institution;

                public String Owner;
            }

        }

        public class AssetInvestment implements java.io.Serializable {

            public String TotalAmount;

            public java.util.ArrayList Items = new java.util.ArrayList();

            public class Item implements java.io.Serializable {
                public String Amount;

                public String AccountNumber;

                public String FinancialType;

                public String FinancialCode;

                public String FinancialDesc;

                public String Institution;

                public String Owner;
            }

        }

        public class AssetPersonal implements java.io.Serializable {

            public String TotalAmount;

            public java.util.ArrayList Items = new java.util.ArrayList();

            public class Item implements java.io.Serializable {
                public String Amount;

                public String AccountNumber;

                public String FinancialType;

                public String FinancialCode;

                public String FinancialDesc;

                public String Institution;

                public String Owner;
            }

        }

        public class AssetSuperannuation implements java.io.Serializable {

            public String TotalAmount;

            public java.util.ArrayList Items = new java.util.ArrayList();

            public class Item implements java.io.Serializable {
                public String Amount;

                public String AccountNumber;

                public String FinancialType;

                public String FinancialCode;

                public String FinancialDesc;

                public String Institution;

                public String Owner;
            }

        }

        /*
         * STRATEGY [ClientView.Strategy.AssetCash.Items.ItemName]
         * [ClientView.Strategy.AssetCash.Items.Projection]
         * [ClientView.Strategy.AssetCash.Items.FinancialDesc]
         * [ClientView.Strategy.AssetCash.Items.Amount]
         */
        public class Strategy implements java.io.Serializable {

            public String Name;

            public Asset asset = new Asset();

            public AssetCash assetCash = new AssetCash();

            public AssetInvestment assetInvestment = new AssetInvestment();

            public AssetPersonal assetPersonal = new AssetPersonal();

            public AssetSuperannuation assetSuperannuation = new AssetSuperannuation();

            public class Asset implements java.io.Serializable {

                public String TotalAmount;

            }

            public class AssetCash implements java.io.Serializable {

                public String TotalAmount;

                public java.util.ArrayList Items = new java.util.ArrayList(); // strategies

                public class Item implements java.io.Serializable {
                    public String ItemName; // Strategy

                    public String Projection; // StrategyModel

                    public String TotalAmount;

                    public String Financials; // list of all financials,
                                                // FinancialDesc+Amount
                }

                public Item createNewItem() {
                    return new Item();
                }

            }

            public class AssetInvestment implements java.io.Serializable {

                public String TotalAmount;

                public java.util.ArrayList Items = new java.util.ArrayList();

                public class Item implements java.io.Serializable {
                    public String ItemName; // Strategy

                    public String Projection; // StrategyModel

                    public String TotalAmount;

                    public String Financials; // list of all financials,
                                                // FinancialDesc+Amount
                }

                public Item createNewItem() {
                    return new Item();
                }

            }

            public class AssetPersonal implements java.io.Serializable {

                public String TotalAmount;

                public java.util.ArrayList Items = new java.util.ArrayList();

                public class Item implements java.io.Serializable {
                    public String ItemName; // Strategy

                    public String Projection; // StrategyModel

                    public String TotalAmount;

                    public String Financials; // list of all financials,
                                                // FinancialDesc+Amount
                }

                public Item createNewItem() {
                    return new Item();
                }

            }

            public class AssetSuperannuation implements java.io.Serializable {

                public String TotalAmount;

                public java.util.ArrayList Items = new java.util.ArrayList();

                public class Item implements java.io.Serializable {
                    public String ItemName; // Strategy

                    public String Projection; // StrategyModel

                    public String TotalAmount;

                    public String Financials; // list of all financials,
                                                // FinancialDesc+Amount
                }

                public Item createNewItem() {
                    return new Item();
                }

            }
            // [ClientView.Strategy.AssetSuperannuation.Items.ItemName]
            // [ClientView.Strategy.AssetSuperannuation.Items.Financials]
            // [ClientView.Strategy.AssetSuperannuation.Items.TotalAmount]
            // [ClientView.Strategy.AssetSuperannuation.TotalAmount]

        }

    }

}
