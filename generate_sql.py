import random

teams = ['ANA','BOS','BUF','CGY','CAR','CHI','COL','CBJ','DAL','DET','EDM','FLA','LAK','MIN','MTL','NSH','NJD','NYI','NYR','OTT','PHI','PIT','SJS','SEA','STL','TBL','TOR','UTA','VAN','VGK','WSH','WPG']
team_names = {'ANA':'Anaheim Ducks','BOS':'Boston Bruins','BUF':'Buffalo Sabres','CGY':'Calgary Flames','CAR':'Carolina Hurricanes','CHI':'Chicago Blackhawks','COL':'Colorado Avalanche','CBJ':'Columbus Blue Jackets','DAL':'Dallas Stars','DET':'Detroit Red Wings','EDM':'Edmonton Oilers','FLA':'Florida Panthers','LAK':'Los Angeles Kings','MIN':'Minnesota Wild','MTL':'Montreal Canadiens','NSH':'Nashville Predators','NJD':'New Jersey Devils','NYI':'New York Islanders','NYR':'New York Rangers','OTT':'Ottawa Senators','PHI':'Philadelphia Flyers','PIT':'Pittsburgh Penguins','SJS':'San Jose Sharks','SEA':'Seattle Kraken','STL':'St. Louis Blues','TBL':'Tampa Bay Lightning','TOR':'Toronto Maple Leafs','UTA':'Utah Hockey Club','VAN':'Vancouver Canucks','VGK':'Vegas Golden Knights','WSH':'Washington Capitals','WPG':'Winnipeg Jets'}

sql = "INSERT INTO shop_item (name, description, price, image_url, category, team_abbrev, featured) VALUES\n"
values = []

# Featured Stars
stars = [
    ('EDM', 'Camisa Connor McDavid 97', 'Camisa oficial Home do Edmonton Oilers - McDavid.', 250.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8478402.jpg', 'Camisas', 'true'),
    ('CHI', 'Camisa Connor Bedard 98', 'Camisa oficial Home do Chicago Blackhawks - Bedard.', 250.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8484144.jpg', 'Camisas', 'true'),
    ('TOR', 'Camisa Auston Matthews 34', 'Camisa oficial Home do Toronto Maple Leafs - Matthews.', 250.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8479318.jpg', 'Camisas', 'true'),
    ('BOS', 'Camisa David Pastrnak 88', 'Camisa oficial Home do Boston Bruins - Pastrnak.', 240.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8477956.jpg', 'Camisas', 'true'),
    ('PIT', 'Camisa Sidney Crosby 87', 'Camisa oficial Home do Pittsburgh Penguins - Crosby.', 240.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8471675.jpg', 'Camisas', 'true'),
    ('NYR', 'Camisa Artemi Panarin 10', 'Camisa oficial Home do New York Rangers - Panarin.', 240.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8478550.jpg', 'Camisas', 'true'),
    ('COL', 'Camisa Nathan MacKinnon 29', 'Camisa oficial Home do Colorado Avalanche - MacKinnon.', 250.00, 'https://cms.nhl.bamgrid.com/images/headshots/current/168x168/8477492.jpg', 'Camisas', 'true')
]

for t, n, d, p, i, c, f in stars:
    values.append(f"('{n}', '{d}', {p}, '{i}', '{c}', '{t}', {f})")

# Generate for all teams
for t in teams:
    name = team_names[t]
    
    # Camisas
    values.append(f"('{name} Home Jersey', 'Camisa Oficial Principal (Home) do {name}.', 180.00, 'https://dummyimage.com/400x400/1e293b/ffffff&text={t}+Home+Jersey', 'Camisas', '{t}', false)")
    values.append(f"('{name} Away Jersey', 'Camisa Oficial Branca (Away) do {name}.', 180.00, 'https://dummyimage.com/400x400/f8fafc/1e293b&text={t}+Away+Jersey', 'Camisas', '{t}', false)")
    values.append(f"('{name} Alternate Jersey', 'Camisa Oficial Alternativa (Third) do {name}.', 190.00, 'https://dummyimage.com/400x400/0f172a/ffffff&text={t}+Alternate', 'Camisas', '{t}', false)")
    
    # Roupas
    values.append(f"('{name} Locker Room Hoodie', 'Moletom Oficial de vestiário do {name}.', 85.00, 'https://dummyimage.com/400x400/334155/ffffff&text={t}+Hoodie', 'Roupas', '{t}', false)")
    values.append(f"('{name} Practice T-Shirt', 'Camiseta de treino oficial.', 40.00, 'https://dummyimage.com/400x400/64748b/ffffff&text={t}+T-Shirt', 'Roupas', '{t}', false)")
    values.append(f"('{name} Winter Jacket', 'Jaqueta de inverno grossa com logo bordado.', 120.00, 'https://dummyimage.com/400x400/0f172a/ffffff&text={t}+Jacket', 'Roupas', '{t}', false)")
    
    # Acessórios
    values.append(f"('{name} Draft Cap', 'Boné Oficial do Draft da NHL.', 35.00, 'https://dummyimage.com/400x400/1e293b/ffffff&text={t}+Cap', 'Acessórios', '{t}', false)")
    values.append(f"('{name} Knit Pom Beanie', 'Gorro de frio com pompom Authentic Pro.', 30.00, 'https://dummyimage.com/400x400/cbd5e1/1e293b&text={t}+Beanie', 'Acessórios', '{t}', false)")
    values.append(f"('{name} Scarf', 'Cachecol de inverno com franjas.', 25.00, 'https://dummyimage.com/400x400/94a3b8/ffffff&text={t}+Scarf', 'Acessórios', '{t}', false)")
    
    # Colecionáveis
    values.append(f"('{name} Official Puck', 'Disco de Jogo Oficial (Puck).', 15.00, 'https://dummyimage.com/400x400/000000/ffffff&text={t}+Puck', 'Colecionáveis', '{t}', false)")
    values.append(f"('{name} Mini Helmet', 'Mini capacete colecionável para estante.', 45.00, 'https://dummyimage.com/400x400/1e293b/ffffff&text={t}+Helmet', 'Colecionáveis', '{t}', false)")
    values.append(f"('{name} Pennant Flag', 'Bandeira flâmula clássica de parede.', 20.00, 'https://dummyimage.com/400x400/f8fafc/1e293b&text={t}+Flag', 'Colecionáveis', '{t}', false)")

# Add the old Leafs items
leafs_old = [
    ('Navy blue hockey cap', 'Boné azul oficial Toronto Maple Leafs - 47 Clean Up Adjustable.', 35.00, 'https://fanatics.frgimages.com/toronto-maple-leafs/mens-47-navy-toronto-maple-leafs-team-clean-up-adjustable-hat_pi4028000_ff_4028803-0c55d82354f805418204_full.jpg?_hv=2&w=400', 'Acessórios', 'TOR', 'false'),
    ('Camiseta Mitchell Marner', 'Camiseta Oficial Mitchell Marner - Breakaway Reversible Player Jersey.', 95.00, 'https://fanatics.frgimages.com/toronto-maple-leafs/womens-fanatics-mitchell-marner-black-toronto-maple-leafs-alternate-premier-breakaway-reversible-player-jersey_pi4299000_altimages_ff_4299173-876faf9b620aac273769alt1_full.jpg?_hv=2&w=900', 'Camisas', 'TOR', 'true'),
    ('Sleep Set Hoodie/Joggers', 'Conjunto de pijama oficial (Moletom e Calça) - Conforto total.', 80.00, 'https://fanatics.frgimages.com/toronto-maple-leafs/mens-concepts-sport-navy-toronto-maple-leafs-big-and-tall-pullover-hoodie-and-joggers-sleep-set_pi4349000_altimages_ff_4349177-c1c184f142c2f8af34a6alt1_full.jpg?_hv=2&w=900', 'Roupas', 'TOR', 'false'),
    ('Mini Helmet Royal Blue', 'Capacete miniatura oficial Toronto Maple Leafs - Royal Blue.', 25.00, 'https://fanatics.frgimages.com/toronto-maple-leafs/toronto-maple-leafs-unsigned-sportstar-royal-blue-mini-helmet_pi4042000_ff_4042655-3fbfd2c4304309ed15e3_full.jpg?_hv=2&w=900', 'Colecionáveis', 'TOR', 'false'),
    ('Knit Hat with Pom', 'Gorro de lã Toronto Maple Leafs com pompom - Authentic Pro.', 30.00, 'https://images.footballfanatics.com/toronto-maple-leafs/womens-fanatics-white/blue-toronto-maple-leafs-authentic-pro-rink-cuffed-knit-hat-with-pom_ss5_p-200897938+u-k0dt6qjecqeuxjkh8iiv+v-6wkf5iqfbrdqd9z4bhsx.jpg?_hv=2&w=400', 'Acessórios', 'TOR', 'false'),
    ('Stanley Cup Puck', 'Disco de hockey (puck) comemorativo - Stanley Cup Champions.', 15.00, 'https://images.footballfanatics.com/toronto-maple-leafs/toronto-maple-leafs-unsigned-1967-stanley-cup-champions-logo-hockey-puck_pi3082000_ff_3082174_full.jpg?_hv=2&w=400', 'Colecionáveis', 'TOR', 'false'),
    ('Mitchell Marner Poster', 'Poster oficial Mitchell Marner - 35.75 x 24.25.', 15.00, 'https://fanatics.frgimages.com/toronto-maple-leafs/mitchell-marner-toronto-maple-leafs-3575-x-2425-player-poster_pi4392000_ff_4392233-baa6d3f1809fbaf9f212_full.jpg?_hv=2&w=400', 'Colecionáveis', 'TOR', 'false')
]

for n, d, p, i, c, t, f in leafs_old:
    values.append(f"('{n}', '{d}', {p}, '{i}', '{c}', '{t}', {f})")

sql += ',\n'.join(values) + ';'

with open('c:/Users/taisl/Desktop/OneDrive/Documentos/spring-nhl/nhl-spring-app/src/main/resources/data.sql', 'w', encoding='utf-8') as f:
    f.write(sql)

print('Data SQL updated with', len(values), 'items!')
