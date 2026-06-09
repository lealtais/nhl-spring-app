import { Game } from '../types';

interface ScoreHeaderProps {
  game: Game;
  accentColor: string;
}

function proxyImageUrl(url: string): string {
  if (!url) return '';
  return `https://images.weserv.nl/?url=${encodeURIComponent(url)}`;
}

export default function ScoreHeader({ game, accentColor }: ScoreHeaderProps) {
  return (
    <div className="score-header" style={{ borderColor: `${accentColor}4D` }}>
      <div className="score-team">
        {game.awayLogo ? (
          <img
            src={proxyImageUrl(game.awayLogo)}
            alt={game.awayTeam}
            className="team-logo-large"
          />
        ) : (
          <div className="team-logo-placeholder">🏒</div>
        )}
        <span className="team-abbrev">{game.awayTeam}</span>
      </div>

      <div className="score-display">
        <span className="score-number">{game.awayScore}</span>
        <span className="score-vs">VS</span>
        <span className="score-number">{game.homeScore}</span>
      </div>

      <div className="score-team">
        {game.homeLogo ? (
          <img
            src={proxyImageUrl(game.homeLogo)}
            alt={game.homeTeam}
            className="team-logo-large"
          />
        ) : (
          <div className="team-logo-placeholder">🏒</div>
        )}
        <span className="team-abbrev">{game.homeTeam}</span>
      </div>
    </div>
  );
}
