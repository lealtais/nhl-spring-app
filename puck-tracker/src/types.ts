export interface Goal {
  eventId: number;
  player: string;
  playerPhoto: string;
  team: string;
  period: number;
  time: string;
  x: number;
  y: number;
  shotType: string;
}

export interface Game {
  id: number;
  date: string;
  homeTeam: string;
  awayTeam: string;
  homeScore: number;
  awayScore: number;
  homeLogo: string;
  awayLogo: string;
  gameState: string;
}

export interface NhlTeam {
  abbrev: string;
  name: string;
  primaryColor: string; // hex color string like '#00205B'
}

export const NHL_TEAMS: NhlTeam[] = [
  { abbrev: 'TOR', name: 'Toronto Maple Leafs', primaryColor: '#00205B' },
  { abbrev: 'MTL', name: 'Montreal Canadiens', primaryColor: '#AF1E2D' },
  { abbrev: 'BOS', name: 'Boston Bruins', primaryColor: '#FFB81C' },
  { abbrev: 'EDM', name: 'Edmonton Oilers', primaryColor: '#041E42' },
  { abbrev: 'VAN', name: 'Vancouver Canucks', primaryColor: '#00205B' },
  { abbrev: 'FLA', name: 'Florida Panthers', primaryColor: '#C8102E' },
  { abbrev: 'NYR', name: 'New York Rangers', primaryColor: '#0038A8' },
  { abbrev: 'CAR', name: 'Carolina Hurricanes', primaryColor: '#CC0000' },
  { abbrev: 'COL', name: 'Colorado Avalanche', primaryColor: '#6F263D' },
  { abbrev: 'DAL', name: 'Dallas Stars', primaryColor: '#006847' },
  { abbrev: 'VGK', name: 'Vegas Golden Knights', primaryColor: '#B4975A' },
  { abbrev: 'CHI', name: 'Chicago Blackhawks', primaryColor: '#CF0A2C' },
  { abbrev: 'PIT', name: 'Pittsburgh Penguins', primaryColor: '#FCB827' },
  { abbrev: 'TBL', name: 'Tampa Bay Lightning', primaryColor: '#002868' },
  { abbrev: 'WSH', name: 'Washington Capitals', primaryColor: '#041E42' },
  { abbrev: 'WPG', name: 'Winnipeg Jets', primaryColor: '#041E42' },
  { abbrev: 'ANA', name: 'Anaheim Ducks', primaryColor: '#F47A38' },
  { abbrev: 'BUF', name: 'Buffalo Sabres', primaryColor: '#002654' },
  { abbrev: 'CGY', name: 'Calgary Flames', primaryColor: '#C8102E' },
  { abbrev: 'CBJ', name: 'Columbus Blue Jackets', primaryColor: '#002654' },
  { abbrev: 'DET', name: 'Detroit Red Wings', primaryColor: '#CE1126' },
  { abbrev: 'LAK', name: 'Los Angeles Kings', primaryColor: '#111111' },
  { abbrev: 'MIN', name: 'Minnesota Wild', primaryColor: '#154734' },
  { abbrev: 'NSH', name: 'Nashville Predators', primaryColor: '#FFB81C' },
  { abbrev: 'NJD', name: 'New Jersey Devils', primaryColor: '#CE1126' },
  { abbrev: 'NYI', name: 'New York Islanders', primaryColor: '#00539B' },
  { abbrev: 'OTT', name: 'Ottawa Senators', primaryColor: '#C8102E' },
  { abbrev: 'PHI', name: 'Philadelphia Flyers', primaryColor: '#F74902' },
  { abbrev: 'SJS', name: 'San Jose Sharks', primaryColor: '#006D75' },
  { abbrev: 'SEA', name: 'Seattle Kraken', primaryColor: '#001628' },
  { abbrev: 'STL', name: 'St. Louis Blues', primaryColor: '#002F87' },
  { abbrev: 'UTA', name: 'Utah Hockey Club', primaryColor: '#6F263D' },
];
