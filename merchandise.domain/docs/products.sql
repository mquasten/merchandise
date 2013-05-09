delete from classification where kind = 'P';
insert into classification ( id, description, kind) values ( 'P-0', 'Agriculture, forestry and fishery products' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-01', 'Products of agriculture, horticulture and market gardening' , 'P', 'P-0');
insert into classification ( id, description, kind, parent_id ) values ( 'P-02', 'Live animals and animal products (excluding meat)' , 'P', 'P-0');
insert into classification ( id, description, kind, parent_id ) values ( 'P-03', 'Forestry and logging products' , 'P', 'P-0');
insert into classification ( id, description, kind, parent_id ) values ( 'P-04', 'Fish and other fishing products' , 'P', 'P-0');
insert into classification ( id, description, kind) values ( 'P-1', 'Ores and minerals; electricity, gas and water' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-11', 'Coal and lignite; peat' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-12', 'Crude petroleum and natural gas' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-13', 'Uranium and thorium ores and concentrates' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-14', 'Metal ores' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-15', 'Stone, sand and clay' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-16', 'Other minerals' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-17', 'Electricity, town gas, steam and hot water' , 'P', 'P-1');
insert into classification ( id, description, kind, parent_id ) values ( 'P-18', 'Natural water' , 'P', 'P-1');
insert into classification ( id, description, kind) values ( 'P-2', 'Food products, beverages and tobacco; textiles, apparel and leather products' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-21', 'Meat, fish, fruit, vegetables, oils and fats' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-22', 'Dairy products and egg products' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-23', 'Grain mill products, starches and starch products; other food products' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-24', 'Beverages' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-25', 'Tobacco products' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-26', 'Yarn and thread; woven and tufted textile fabrics' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-27', 'Textile articles other than apparel' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-28', 'Knitted or crocheted fabrics; wearing apparel' , 'P', 'P-2');
insert into classification ( id, description, kind, parent_id ) values ( 'P-29', 'Leather and leather products; footwear' , 'P', 'P-2');
insert into classification ( id, description, kind) values ( 'P-3', 'Other transportable goods, except metal products, machinery and equipment' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-31', 'Products of wood, cork, straw and plaiting materials' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-32', 'Pulp, paper and paper products; printed matter and related articles' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-33', 'Coke oven products; refined petroleum products; nuclear fuel' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-34', 'Basic chemicals' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-35', 'Other chemical products; man' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-36', 'Rubber and plastics products' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-37', 'Glass and glass products and other non' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-38', 'Furniture; other transportable goods n.e.c.' , 'P', 'P-3');
insert into classification ( id, description, kind, parent_id ) values ( 'P-39', 'Wastes or scraps' , 'P', 'P-3');
insert into classification ( id, description, kind) values ( 'P-4', 'Metal products, machinery and equipment' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-41', 'Basic metals' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-42', 'Fabricated metal products, except machinery and equipment' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-43', 'General' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-44', 'Special' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-45', 'Office, accounting and computing machinery' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-46', 'Electrical machinery and apparatus' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-47', 'Radio, television and communication equipment and apparatus' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-48', 'Medical appliances, precision and optical instruments, watches and clocks' , 'P', 'P-4');
insert into classification ( id, description, kind, parent_id ) values ( 'P-49', 'Transport equipment' , 'P', 'P-4');
insert into classification ( id, description, kind) values ( 'P-5', 'Constructions and construction services' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-53', 'Constructions' , 'P', 'P-5');
insert into classification ( id, description, kind, parent_id ) values ( 'P-54', 'Construction services' , 'P', 'P-5');
insert into classification ( id, description, kind) values ( 'P-6', 'Distributive trade services; accommodation, food and beverage serving services; transport services; and electricity, gas and water distribution services' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-61', 'Wholesale trade services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-62', 'Retail trade services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-63', 'Accommodation, food and beverage services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-64', 'Passenger transport services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-65', 'Freight transport services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-66', 'Rental services of transport vehicles with operators' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-67', 'Supporting transport services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-68', 'Postal and courier services' , 'P', 'P-6');
insert into classification ( id, description, kind, parent_id ) values ( 'P-69', 'Electricity, gas and water distribution (on own account)' , 'P', 'P-6');
insert into classification ( id, description, kind) values ( 'P-7', 'Financial and related services; real estate services; and rental and leasing services' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-71', 'Financial and related services' , 'P', 'P-7');
insert into classification ( id, description, kind, parent_id ) values ( 'P-72', 'Real estate services' , 'P', 'P-7');
insert into classification ( id, description, kind, parent_id ) values ( 'P-73', 'Leasing or rental services without operator' , 'P', 'P-7');
insert into classification ( id, description, kind) values ( 'P-8', 'Business and production services' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-81', 'Research and development services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-82', 'Legal and accounting services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-83', 'Other professional, technical and business services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-84', 'Telecommunications, broadcasting and information supply services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-85', 'Support services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-86', 'Support services to agriculture, hunting, forestry, fishing, mining and utilities' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-87', 'Maintenance, repair and installation (except construction) services' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-88', 'Manufacturing services on physical inputs owned by others' , 'P', 'P-8');
insert into classification ( id, description, kind, parent_id ) values ( 'P-89', 'Other manufacturing services; publishing, printing and reproduction services; materials recovery services' , 'P', 'P-8');
insert into classification ( id, description, kind) values ( 'P-9', 'Community, social and personal services' , 'P');
insert into classification ( id, description, kind, parent_id ) values ( 'P-91', 'Public administration and other services provided to the community as a whole; compulsory social security services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-92', 'Education services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-93', 'Human health and social care services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-94', 'Sewage and waste collection, treatment and disposal and other environmental protection services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-95', 'Services of membership organizations' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-96', 'Recreational, cultural and sporting services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-97', 'Other services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-98', 'Domestic services' , 'P', 'P-9');
insert into classification ( id, description, kind, parent_id ) values ( 'P-99', 'Services provided by extraterritorial organizations and bodies' , 'P', 'P-9');